package dst2.ejb;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import dst2.exception.NotEnoughCPUsAvailableException;
import dst2.exception.NotLoggedInException;
import dst2.exception.ResourceNotAvailableException;
import dst2.model.AuditLog;
import dst2.model.Cluster;
import dst2.model.Computer;
import dst2.model.Environment;
import dst2.model.Execution;
import dst2.model.FunctionParam;
import dst2.model.Grid;
import dst2.model.Job;
import dst2.model.JobStatus;
import dst2.model.User;

@Stateful
public class JobManagementBean implements JobManagementBeanRemote {

	// @PersistenceContext
	// EntityManager em;

	@Resource
	EntityManagerFactory emf;
	@Resource
	UserTransaction utx;

	private User user;
	private List<Job> jobList = new ArrayList<Job>();
	private List<Computer> assignedComputers = new ArrayList<Computer>();
	private HashMap<Long, Integer> gridJobCount;

	@Override
	public void loginUser(String username, String password) {

		EntityManager em = emf.createEntityManager();

		MessageDigest md = null;
		byte[] hash = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: security! white-list-regex for username?
		Query query = em.createQuery(
				"SELECT u FROM User u WHERE u.username LIKE :name")
				.setParameter("name", username);
		User tempUser = (User) query.getSingleResult();
		try {
			hash = md.digest(password.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (hash == user.getPassword()) {
			user = tempUser;
		}
	}

	@Override
	public void addJob(Long grid_id, Integer numCPUs, String workflow,
			List<String> params) throws NotEnoughCPUsAvailableException {

		EntityManager em = emf.createEntityManager();

		// TODO maybe make a query of this?
		Integer availCPUs = 0;

		Grid grid = em.find(Grid.class, grid_id);

		Set<Cluster> clusterList = grid.getClusterList();

		for (Cluster cluster : clusterList) {
			Set<Computer> computerList = cluster.getComputerList();
			for (Computer computer : computerList) {
				// TODO: check if necessary
				em.detach(computer);
				availCPUs += computer.getCpus();
			}
		}

		if (availCPUs <= numCPUs) {
			throw new NotEnoughCPUsAvailableException("needed: "
					+ numCPUs.toString() + ", have: " + availCPUs);
		} else {

			Job job = new Job(false);
			Environment env = new Environment(workflow, params);
			Execution exec = new Execution();

			job.setEnvironment(env);
			job.setExecution(exec);

			Set<Computer> execComputerList = new HashSet<Computer>();

			overAll: for (Cluster cluster : clusterList) {
				Set<Computer> computerList = cluster.getComputerList();
				for (Computer computer : computerList) {
					if (assignedComputers.contains(computer))
						break;
					for (Execution tempExec : computer.getExecutionList()) {
						if (tempExec.getEnd() != null)
							break;
						execComputerList.add(computer);
						assignedComputers.add(computer);
						if (gridJobCount.containsKey(grid_id)) {
							gridJobCount.put(grid_id,
									gridJobCount.get(grid_id) + 1);
						} else
							gridJobCount.put(grid_id, 1);
						numCPUs -= computer.getCpus();
						if (numCPUs <= 0)
							break overAll;
					}
				}
			}

			exec.setComputerList(execComputerList);

			jobList.add(job);
		}
		em.close();
	}

	// TODO: check transaction management
	@Override
	@Remove(retainIfException = true)
	public void checkout() throws NotLoggedInException,
			ResourceNotAvailableException {

		if (user == null) {
			throw new NotLoggedInException();
		}

		EntityManager em = emf.createEntityManager();

		try {
			utx.begin();
			for (Job job : jobList) {
				Execution exec = job.getExecution();
				exec.setStart(new Date());
				exec.setStatus(JobStatus.SCHEDULED);

				job.setUser(user);
				user.getJobList().add(job);

				for (Computer computer : exec.getComputerList()) {
					Computer compInDb = em.find(Computer.class,
							computer.getId(), LockModeType.WRITE);
					if (compInDb == null) {
						utx.rollback();
						throw new ResourceNotAvailableException(
								"Computer not in DB anymore");
					}
					for (Execution tempExec : computer.getExecutionList()) {
						if (tempExec.getEnd() != null) {
							utx.rollback();
							throw new ResourceNotAvailableException("Computer "
									+ compInDb.toString()
									+ " is no longer avaiable");
						}
					}
				}

				em.persist(job);
				em.persist(exec);
				em.merge(user);
				for (Computer computer : exec.getComputerList()) {
					computer.getExecutionList().add(exec);
					em.merge(computer);
				}
			}
			utx.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				utx.rollback();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
			} finally {
				e.printStackTrace();
			}
		} finally {
			em.close();
		}

	}

	@Override
	public HashMap<Long, Integer> getJobList() {
		return this.gridJobCount;
	}

	@Override
	public void clearJobList(Long grid_id) {
		EntityManager em = emf.createEntityManager();

		// TODO check if easier way
		for (Job job : jobList) {
			for (Computer comp : job.getExecution().getComputerList()) {
				if (comp.getCluster().getGrid().getId() == grid_id) {
					jobList.remove(job);
				}
			}
		}

		if (gridJobCount.containsKey(grid_id)) {
			gridJobCount.put(grid_id, 0);
		}

		em.close();
	}

	@AroundInvoke
	public Object jobManagementAuditInterceptor(InvocationContext ctx)
			throws Exception {
		EntityManager em = emf.createEntityManager();

		int i = 0;
		AuditLog auditLog = new AuditLog();

		auditLog.setMethodName(ctx.getMethod().getName());
		auditLog.setDate(new Date());
		for (Object o : ctx.getParameters()) {
			FunctionParam param = new FunctionParam();
			param.setClassName(o.getClass().getName());
			param.setIndex(i++);
			param.setValue(o.toString());
			auditLog.getParams().add(param);
		}

		em.persist(auditLog);

		em.close();

		return ctx.proceed();
	}
}
