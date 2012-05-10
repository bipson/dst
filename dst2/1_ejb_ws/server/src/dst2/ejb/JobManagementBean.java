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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.ejb.interfaces.JobManagementBeanRemote;
import dst2.exception.NotEnoughCPUsAvailableException;
import dst2.exception.NotLoggedInException;
import dst2.exception.ResourceNotAvailableException;
import dst2.model.AuditLog;
import dst2.model.Computer;
import dst2.model.Environment;
import dst2.model.Execution;
import dst2.model.FunctionParam;
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
		User tempUser;

		try {
			tempUser = query.getSingleResult();
		} catch (NoResultException e) {
			return;
		}

		try {
			if (MessageDigest.isEqual(md.digest(password.getBytes("UTF8")),
					tempUser.getPassword())) {
				user = tempUser;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addJob(Long grid_id, Integer numCPUs, String workflow,
			List<String> params) throws NotEnoughCPUsAvailableException {

		// TODO simplify this process ASAP
		Integer availCPUs = 0;
		List<Computer> freeComputers = new ArrayList<Computer>();

		TypedQuery<Computer> computersOnGridQuery = em
				.createQuery(
						"SELECT c FROM Computer c JOIN c.cluster clust JOIN clust.grid g WHERE g.id = ?1",
						Computer.class).setParameter(1, grid_id);

		// Check if Computer free (exclude already assigned Computers)
		computerLoop: for (Computer comp : computersOnGridQuery.getResultList()) {
			if (assignedComputers.contains(comp))
				continue;
			for (Execution exec : comp.getExecutionList()) {
				if (exec.getEnd() == null)
					continue computerLoop;
			}
			availCPUs += comp.getCpus();
			freeComputers.add(comp);
		}

		if (availCPUs < numCPUs) {
			throw new NotEnoughCPUsAvailableException("needed: "
					+ numCPUs.toString() + ", have: " + availCPUs);
		} else {

			Job job = new Job(false);
			Environment env = new Environment(workflow, params);
			Execution exec = new Execution();

			job.setEnvironment(env);
			job.setExecution(exec);

			Set<Computer> execComputerList = new HashSet<Computer>();

			for (Computer computer : freeComputers) {

				assignedComputers.add(computer);
				// TODO: check if necessary
				em.detach(computer);
				execComputerList.add(computer);
				computer.getExecutionList().add(exec);

				if (gridJobCount.containsKey(grid_id)) {
					gridJobCount.put(grid_id, gridJobCount.get(grid_id) + 1);
				} else {
					gridJobCount.put(grid_id, 1);
				}

				numCPUs -= computer.getCpus();
				if (numCPUs <= 0)
					break;
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
						LockModeType.PESSIMISTIC_WRITE);
				if (compInDb == null) {
					context.setRollbackOnly();
					throw new ResourceNotAvailableException(
							"Computer not in DB anymore");
				}
				for (Execution tempExec : compInDb.getExecutionList()) {
					if (tempExec.getEnd() == null) {
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
	public Integer getJobList(Long grid_id) {
		if (gridJobCount.containsKey(grid_id))
			return gridJobCount.get(grid_id);
		else
			return 0;
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
			gridJobCount.remove(grid_id);
		}
	}

	@AroundInvoke
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Object jobManagementAuditInterceptor(InvocationContext ctx)
			throws Exception {
		int i = 0;
		AuditLog auditLog = new AuditLog();
		auditLog.setMethodName(ctx.getMethod().getName());
		auditLog.setDate(new Date());
		if (ctx.getParameters() != null) {
			for (Object o : ctx.getParameters()) {
				FunctionParam param = new FunctionParam();
				// if (o == null) {
				// param.setClassName("null");
				// param.setIndex(i++);
				// param.setValue("null");
				// } else {
				param.setClassName(o.getClass().getName());
				param.setIndex(i++);
				param.setValue(o.toString());
				// }
				auditLog.getParams().add(param);
			}
		}

		try {
			Object result = ctx.proceed();
			if (result != null) {
				auditLog.setResult(result.toString());
			} else {
				auditLog.setResult(null);
			}
			return result;
		} catch (Exception e) {
			auditLog.setResult(e.toString());
			throw e;
		} finally {
			em.persist(auditLog);
			em.flush();
		}
	}
}
