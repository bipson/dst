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
import javax.ejb.EJBContext;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.exception.NotEnoughCPUsAvailableException;
import dst2.exception.NotLoggedInException;
import dst2.exception.ResourceNotAvailableException;
import dst2.model.AuditLog;
import dst2.model.Cluster;
import dst2.model.Computer;
import dst2.model.Environment;
import dst2.model.Execution;
import dst2.model.Grid;
import dst2.model.Job;
import dst2.model.JobStatus;
import dst2.model.User;

@Stateful
public class JobManagementBean implements JobManagementBeanRemote {

	@PersistenceContext
	EntityManager em;

	@Resource
	EJBContext context;

	private User user;
	private List<Job> jobList = new ArrayList<Job>();
	private List<Computer> assignedComputers = new ArrayList<Computer>();
	private HashMap<Long, Integer> gridJobCount = new HashMap<Long, Integer>();

	@Override
	public void loginUser(String username, String password) {

		MessageDigest md = null;
		byte[] hash = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: security! white-list-regex for username?
		TypedQuery<User> query = em.createQuery(
				"SELECT u FROM User u WHERE u.username = :name", User.class)
				.setParameter("name", username);
		User tempUser = query.getSingleResult();

		if (user != null) {
			try {
				if (MessageDigest.isEqual(md.digest(password.getBytes("UTF8")),
						user.getPassword())) {
					user = tempUser;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void addJob(Long grid_id, Integer numCPUs, String workflow,
			List<String> params) throws NotEnoughCPUsAvailableException {

		// TODO maybe make a query of this?
		Integer availCPUs = 0;

		Grid grid = em.find(Grid.class, grid_id);

		if (grid == null) {
			throw new NotEnoughCPUsAvailableException("Grid with grid_id "
					+ grid_id + " does not exist :(");
		}

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
	}

	// TODO: check transaction management
	@Override
	@Remove(retainIfException = true)
	public void checkout() throws NotLoggedInException,
			ResourceNotAvailableException {

		if (user == null) {
			throw new NotLoggedInException();
		}

		for (Job job : jobList) {
			Execution exec = job.getExecution();
			exec.setStart(new Date());
			exec.setStatus(JobStatus.SCHEDULED);

			job.setUser(user);
			user.getJobList().add(job);

			for (Computer computer : exec.getComputerList()) {
				Computer compInDb = em.find(Computer.class, computer.getId(),
						LockModeType.WRITE);
				if (compInDb == null) {
					context.setRollbackOnly();
					throw new ResourceNotAvailableException(
							"Computer not in DB anymore");
				}
				for (Execution tempExec : computer.getExecutionList()) {
					if (tempExec.getEnd() != null) {
						context.setRollbackOnly();
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
			em.flush();
		}
	}

	@Override
	public HashMap<Long, Integer> getJobList() {
		return this.gridJobCount;
	}

	@Override
	public void clearJobList(Long grid_id) {
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

	}

	@AroundInvoke
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Object jobManagementAuditInterceptor(InvocationContext ctx)
			throws Exception {
		int i = 0;
		AuditLog auditLog = new AuditLog();
		//
		// auditLog.setMethodName(ctx.getMethod().getName());
		// auditLog.setDate(new Date());
		// for (Object o : ctx.getParameters()) {
		// FunctionParam param = new FunctionParam();
		// if (o == null) {
		// param.setClassName("null");
		// param.setIndex(i++);
		// param.setValue("null");
		// } else {
		// param.setClassName(o.getClass().getName());
		// param.setIndex(i++);
		// param.setValue("null");
		// param.setValue(o.toString());
		// }
		// System.out.println(o);
		// auditLog.getParams().add(param);
		// }
		//
		// em.persist(auditLog);

		return ctx.proceed();
	}
}
