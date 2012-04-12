package dst1;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.timer.Timer;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;

import dst1.db.*;
import dst1.interceptor.SQLInterceptor;
import dst1.listener.DefaultListener;
import dst1.model.*;
import dst1.query.JobCriteria;

public class Main {
	
	private Main() {
		super();
	}

	public static void main(String[] args) {
		
		// Init EntityManager for DAOs
		GenericDao.initEntityManagerFactory(Persistence.createEntityManagerFactory("grid"));
		
		dst01();
		dst02a();
		dst02b();
		dst02c();
		dst03();
		dst04a();
		dst04b();
		dst04c();
		dst04d();
		dst05a();
		dst05b();
		dst05c();
		
		GenericDao.shutdown();
	}

	public static void dst01() {
		
		System.out.println("=====================");
		System.out.println("========= 01 ========");
		System.out.println("=====================");
		
		final GenericDao<Admin, Long> adminDao =
				new GenericDao<Admin, Long>(Admin.class);
		final GenericDao<Cluster, Long> clusterDao =
				new GenericDao<Cluster, Long>(Cluster.class);
		final GenericDao<Computer, Long> computerDao =
				new GenericDao<Computer, Long>(Computer.class);
		final GenericDao<Environment, Long> environmentDao =
				new GenericDao<Environment, Long>(Environment.class);
		final GenericDao<Execution, Long> executionDao =
				new GenericDao<Execution, Long>(Execution.class);
		final GenericDao<Grid, Long> gridDao =
				new GenericDao<Grid, Long>(Grid.class);
		final GenericDao<Membership, MembershipPK> membershipDao =
				new GenericDao<Membership, MembershipPK>(Membership.class);
 		final GenericDao<Job, Long> jobDao =
 				new GenericDao<Job, Long>(Job.class);
		final GenericDao<User, Long> userDao =
				new GenericDao<User, Long>(User.class);
		
		// Helpers
		long now = System.currentTimeMillis();
		
		// Add some Test-entities
		System.out.println("Will now add some Test-Entities");

		// Environments
		Environment env1 = new Environment("abcd", new LinkedList<String>(Arrays.asList("abc", "cde")));
		Environment env2 = new Environment("efghi", new LinkedList<String>(Arrays.asList("efg", "hij")));
		
		environmentDao.persist(env1);
		environmentDao.persist(env2);
		
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// Users
		User user1 = null, user2 = null;
		try {
			user1 = new User("gacksi", md.digest("foo1".getBytes("UTF-8")), "123", "8000");
			user2 = new User("quacksi", md.digest("foo2".getBytes("UTF-8")), "12345", "8000");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		user1.setFirstName("Alfred");
		user1.setLastName("Gacksi");
		user1.setAddress(new Address("gacksiStreet", "gacksiCity", "12390"));
		
		user2.setFirstName("Alfredus");
		user2.setLastName("Quacksi");
		user2.setAddress(new Address("quacksiStreet", "quacksiCity", "1234590"));
		
		userDao.persist(user1);
		userDao.persist(user2);
		
		// Grids ( + Memberships)
		Grid grid1 = new Grid("grid1", "cellar1", BigDecimal.valueOf(300));
		Grid grid2 = new Grid("grid2", "cellar2", BigDecimal.valueOf(800));
		
		gridDao.persist(grid1);
		gridDao.persist(grid2);
		
		Membership membership1 = new Membership(new Date(), (Double.valueOf(123)));
		
		membership1.setGrid(grid1);
		membership1.setUser(user1);
		
		user1.getMembershipList().add(membership1);
		grid1.getMembershipList().add(membership1);
		
		Membership membership2 = new Membership(new Date(now - (5L * Timer.ONE_WEEK)), (Double.valueOf(456)));
		
		membership2.setGrid(grid2);
		membership2.setUser(user2);
		
		user2.getMembershipList().add(membership2);
		grid2.getMembershipList().add(membership2);
		
		userDao.persist(user1);
		userDao.persist(user2);
		
		membershipDao.persist(membership1);
		membershipDao.persist(membership2);
		
		// Admins
		Admin admin1 = null, admin2 = null;
		
		admin1 = new Admin();
		admin1.setFirstName("a");
		admin1.setLastName("dmin");
		Address address3 = new Address("admin1Street", "admin1City", "11190");
		admin1.setAddress(address3);
		
		admin2 = new Admin();
		admin2.setFirstName("b");
		admin2.setLastName("dmin");
		Address address4 = new Address("admin2Street", "admin2City", "22290");
		admin2.setAddress(address4);
		
		adminDao.persist(admin1);
		adminDao.persist(admin2);
		
		// Clusters
		Cluster cluster1 = null, cluster2 = null;
		
		cluster1 = new Cluster("Clust1", new Date(now - (8L * 52L * Timer.ONE_WEEK)),
				new Date(now + (3L * 52L * Timer.ONE_WEEK)));
		
		cluster1.setAdmin(admin1);
		cluster1.setGrid(grid1);
		
		grid1.getClusterList().add(cluster1);
		admin1.getClusterList().add(cluster1);
		
		cluster2 = new Cluster("Clust2", new Date(now - (6L * 52L * Timer.ONE_WEEK)),
				new Date(now + (5L * 52L * Timer.ONE_WEEK)));
		
		cluster2.setAdmin(admin2);
		cluster2.setGrid(grid2);
		
		grid2.getClusterList().add(cluster2);
		admin2.getClusterList().add(cluster2);
		
		clusterDao.persist(cluster1);
		clusterDao.persist(cluster2);
		
		// Cluster Children
		cluster1.getClusterChildren().add(cluster2);
		
		clusterDao.persist(cluster1);
		clusterDao.persist(cluster2);
		
		// Computers
		Computer computer1 = new Computer("comp1", 6, "AUT-VIE", new Date(), new Date());
		Computer computer2 = new Computer("comp2", 6, "AUT-VIE", new Date(), new Date());
		Computer computer3 = new Computer("comp3", 2, "cellar2", new Date(), new Date());
		Computer computer4 = new Computer("comp4", 1, "cellar2", new Date(), new Date());
		
		computer1.setCluster(cluster1);
		computer2.setCluster(cluster1);
		computer3.setCluster(cluster2);
		computer4.setCluster(cluster2);

		computerDao.persist(computer1);
		computerDao.persist(computer2);
		computerDao.persist(computer3);
		computerDao.persist(computer4);
		
		cluster1.getComputerList().add(computer1);
		cluster1.getComputerList().add(computer2);
		cluster2.getComputerList().add(computer3);
		cluster2.getComputerList().add(computer4);
		
		// Jobs
		Job job1 = new Job(true);
		Job job2 = new Job(false);
		
		job1.setEnvironment(env1);
		job1.setUser(user1);		
		
		job2.setEnvironment(env2);
		job2.setUser(user2);
		
		// Executions
		Execution exec1 = new Execution(
				new Date(), new Date(now + 3L * Timer.ONE_WEEK), JobStatus.FINISHED);
		Execution exec2 = new Execution(
				new Date(now + 2L * Timer.ONE_WEEK), new Date(now + 4L * Timer.ONE_WEEK), JobStatus.SCHEDULED);
		
		// Don't like the look of this, shouldn't this be defined through grid-memberships?
		Set<Computer> user1GridComp = new HashSet<Computer>();
		user1GridComp.add(computer1);
		user1GridComp.add(computer2);
		user1GridComp.add(computer3);
		user1GridComp.add(computer4);
		
		exec1.setComputerList(user1GridComp);
		
		// Don't like the look of this, shouldn't this be defined through grid-memberships?
		Set<Computer> user2GridComp = new HashSet<Computer>();
		user2GridComp.add(computer3);
		user2GridComp.add(computer4);
		
		exec2.setComputerList(user2GridComp);
		
		job1.setExecution(exec1);
		exec1.setJob(job1);
		user1.getJobList().add(job1);
		job2.setExecution(exec2);
		exec2.setJob(job2);
		user2.getJobList().add(job2);
		
		jobDao.persist(job1);
		jobDao.persist(job2);
		
		executionDao.persist(exec1);
		executionDao.persist(exec2);
		
		userDao.persist(user1);
		userDao.persist(user2);
		
		computer1.getExecutionList().add(exec1);
		computer2.getExecutionList().add(exec1);
		computer3.getExecutionList().add(exec1);
		computer4.getExecutionList().add(exec1);

		computer3.getExecutionList().add(exec2);
		computer4.getExecutionList().add(exec2);
		
		System.out.println("Added Test Entities to Database");
		
		// Retrieve some of the Entities
		System.out.println("========================");
		System.out.println("Retrieving some Entities");
		
		User testUser = userDao.get(user1.getId());
		System.out.println("testUser: " +testUser.toString());
		System.out.println("testUser-address: " +testUser.getAddress().toString());
		System.out.println("testUser-memberships: " +testUser.getMembershipList());
		System.out.println("testUser-jobs: " +testUser.getJobList());
		System.out.println("--->");
		
		System.out.println("jumping to grid in first membership in list...");
		Membership testMembership = (Membership)(testUser.getMembershipList().toArray())[0];
		Grid testGrid = testMembership.getGrid();
		System.out.println("testuser-grid: " +testGrid.toString());
		System.out.println("testuser-grid-membership: " +testGrid.getMembershipList().toString());
		System.out.println("testuser-grid-Clusters: " +testGrid.getClusterList().toString());
		System.out.println("--->");
		
		System.out.println("jumping to first cluster in cluster-list...");
		Cluster testCluster = (Cluster)(testGrid.getClusterList().toArray()[0]);
		System.out.println("testcluster: " +testCluster.toString());
		System.out.println("testcluster-computers: " +testCluster.getComputerList());
		System.out.println("testcluster-grids: " +testCluster.getGrid().toString());
		System.out.println("testcluster-admin: " +testCluster.getAdmin().toString());
		System.out.println("testcluster-cluster_children: " +testCluster.getClusterChildren().toString());
		System.out.println("--->");
		
		System.out.println("jumping to first computer in computer-list...");
		Computer testComputer = (Computer)(testCluster.getComputerList().toArray()[0]);
		System.out.println("testComputer: " +testComputer.toString());
		System.out.println("testComputer-clusters: " +testComputer.getCluster());
		System.out.println("testComputer-executions: " +testComputer.getExecutionList());
		System.out.println("--->");
		
		System.out.println("jumping to first exeuction in execution-list...");
		Execution testExecution = (Execution)(testComputer.getExecutionList().toArray()[0]);
		System.out.println("testExecution: " +testExecution.toString());
		System.out.println("testExecution-job: " +testExecution.getJob().toString());
		System.out.println("testExecution-computers: " +testExecution.getComputerList().toString());
		System.out.println("--->");
		
		System.out.println("jumping to first job in job-list...");
		Job testJob = (Job)(testExecution.getJob());
		System.out.println("testJob: " +testJob.toString());
		System.out.println("testJob-user: " +testJob.getUser().toString());
		System.out.println("testJob-exec: " +testJob.getExecution().toString());
		System.out.println("testJob-env: " +testJob.getEnvironment().toString());
		
		// Update some entities
		System.out.println("========================");
		System.out.println("Will now update some entities...");
		
		Admin testAdmin = adminDao.get(admin2.getId());
		System.out.println("testCluster is now: "+ testCluster.toString());
		System.out.println("testCluster-admin is now: "+ testCluster.getAdmin().toString());
		System.out.println("testAdmin is now: "+ testAdmin.toString());
		System.out.println("testAdmin-cluster is now: "+ testAdmin.getClusterList().toString());
		
		testCluster.setAdmin(testAdmin);
		testAdmin.getClusterList().add(testCluster);
		clusterDao.update(testCluster);
		adminDao.update(testAdmin);
		System.out.println("Changed testCluster-admin to other admin");
		
		System.out.println("show it:");
		testCluster = clusterDao.get(testCluster.getId());
		System.out.println("testCluster is now: "+ testCluster.toString());
		testAdmin = adminDao.get(admin2.getId());
		System.out.println("testCluster-admin is now: "+ testCluster.getAdmin().toString());
		System.out.println("testAdmin-clusters is now: "+ testAdmin.getClusterList().toString());
		
		Environment testEnvironment = environmentDao.get(env1.getId());
		System.out.println("testEnvironment is now: "+ testEnvironment.toString());
		System.out.println("testEnvironment-params is now: "+ testEnvironment.getParams().toString());
		
		testEnvironment.getParams().add("ulabula");
		environmentDao.update(testEnvironment);
		
		testEnvironment = environmentDao.get(env1.getId());
		System.out.println("testEnvironment is now: "+ testEnvironment.toString());
		System.out.println("testEnvironment-params is now: "+ testEnvironment.getParams().toString());

		// Delete some entities
		System.out.println("========================");
		System.out.println("Will now delete some Test-Entities again");
		
		Job testJob2 = jobDao.get(job2.getId());
		Execution testExec = executionDao.get(testJob2.getExecution().getId());
		Iterator<Computer> compsIt = testExec.getComputerList().iterator();
		while (compsIt.hasNext()) {
			Computer comp = compsIt.next();
			comp.getExecutionList().remove(testExec);
			computerDao.update(comp);
		}
		jobDao.delete(testJob2);
		System.out.println("Removed job");
		
		Computer testComputer2 = computerDao.get(computer3.getId());
		Iterator<Execution> execs = testComputer2.getExecutionList().iterator();
		while (execs.hasNext()) {
			Execution ex = execs.next();
			ex.getComputerList().remove(testComputer2);
			testComputer2.getExecutionList().remove(ex);
		}
		computerDao.delete(testComputer2);
		System.out.println("Removed computer");
		
	}

	public static void dst02a() {
		
		System.out.println("=====================");
		System.out.println("========= 2a ========");
		System.out.println("=====================");
		
		long now = System.currentTimeMillis();
		
		TypedQuery<User> userFind = GenericDao.getEntityManager().createNamedQuery("User.find", User.class);
		TypedQuery<User> userMax = GenericDao.getEntityManager().createNamedQuery("User.mostActive", User.class);
		
		userFind.setParameter("gridname", "grid1");
		userFind.setParameter("jobcount", 1l);
		
		List<User> foo = userFind.getResultList();
		
		System.out.println("findUser-Result: "+foo.toString());
		
		foo = userMax.getResultList();
		System.out.println("findMaxUser-Result: "+foo.toString());
		
	}

	public static void dst02b() {
		
		System.out.println("=====================");
		System.out.println("========= 2b ========");
		System.out.println("=====================");
		
		HibernateEntityManager hem = GenericDao.getEntityManager().unwrap(HibernateEntityManager.class);
		Session hSession = hem.getSession();
		
		Query query = hSession.getNamedQuery("findVienna");
		
		@SuppressWarnings("unchecked")
		List<Computer> computers = (List<Computer>) query.list();
		
		System.out.println("Result: "+ computers.toString());
		
		long count = 0;
		
		for (Computer computer: computers) {
			for (Execution exec: computer.getExecutionList()) {
				count += exec.getEnd().getTime() - exec.getStart().getTime();
			}
		}
		
		System.out.println("Sum of computation time: " + count +"ms");
		
	}

	public static void dst02c() {
		
		System.out.println("=====================");
		System.out.println("========= 2c ========");
		System.out.println("=====================");
		
		final GenericDao<Execution, Long> executionDao =
				new GenericDao<Execution, Long>(Execution.class);
		
		System.out.println("Jobs by User and Workflow: "+JobCriteria.byUserWorkflow("gacksi", "abcd"));

		System.out.println("Jobs by User and Workflow that does not exist: "+JobCriteria.byUserWorkflow("gacksi", "abcde"));
		
		Job jobX = new Job();
		Execution execX = new Execution();
		
		Execution ex = executionDao.get(1l);
		
		execX.setStart(ex.getStart());
		execX.setEnd(ex.getEnd());
		
		execX.setJob(jobX);
		
		jobX.setExecution(execX);
		
		System.out.println("Jobs by example: "+JobCriteria.byExample(jobX));
	}

	public static void dst03() {
		
		System.out.println("=====================");
		System.out.println("========= 03 ========");
		System.out.println("=====================");

	}

	public static void dst04a() {
		long now = System.currentTimeMillis();

		System.out.println("=====================");
		System.out.println("========= 4a ========");
		System.out.println("=====================");
		
		final GenericDao<Environment, Long> environmentDao =
				new GenericDao<Environment, Long>(Environment.class);
		final GenericDao<User, Long> userDao =
				new GenericDao<User, Long>(User.class);
		final GenericDao<Job, Long> jobDao =
				new GenericDao<Job, Long>(Job.class);
		final GenericDao<Execution, Long> execDao =
				new GenericDao<Execution, Long>(Execution.class);
		
		Environment env = new Environment("efghdfai", new LinkedList<String>(Arrays.asList("efgad", "hdfaij")));
		
		environmentDao.persist(env);
		Job jobX = new Job(false);
		System.out.println("Job is now new!");
		
		jobX.setEnvironment(env);
		
		User user = userDao.get(1l);
		jobX.setUser(user);
		jobX.setEnvironment(env);
		user.getJobList().add(jobX);
		
		Execution exec = new Execution(
				new Date(), new Date(now + 5L * Timer.ONE_WEEK), JobStatus.RUNNING);
		
		jobX.setExecution(exec);

		//Will now Persist the new Job
		jobDao.persist(jobX);

		userDao.update(user);	//We need to update user, so association is saved correctly
		System.out.println("New Job persisted: "+ jobX.toString());
		System.out.println("Job is now managed!");
		
		//Will now Detach the Job
		GenericDao.getEntityManager().detach(jobX);
		System.out.println("Job is now detached!");
		
		jobX.setPaid(true);
		System.out.println("Changed something...");
		
		//Will now Re-attach the Job
		jobX = jobDao.update(jobX);
		System.out.println("Job is now managed again!");
		
		//Will retrieve Job
		Job jobY = jobDao.get(jobX.getId());
		System.out.println("Retrieved this exact Job!");
		
		//Will now Remove Job
		jobDao.delete(jobY);
		System.out.println("Job is now removed");
		
	}

	public static void dst04b() {
		System.out.println("=====================");
		System.out.println("========= 4b ========");
		System.out.println("=====================");
		
		final GenericDao<Computer, Long> computerDao =
				new GenericDao<Computer, Long>(Computer.class);
		
		Computer computer = computerDao.get(1l);
		
		System.out.println("Computer creation time: "+computer.getCreation().toString());
		System.out.println("Computer update time: "+computer.getLastUpdate().toString());
		
		computer.setCpus(8);
		
		computerDao.update(computer);
		
		System.out.println("Computer update time: "+computer.getLastUpdate().toString());
	}

	public static void dst04c() {
		System.out.println("=====================");
		System.out.println("========= 4c ========");
		System.out.println("=====================");
		
		System.out.println("Load Operations:			" + DefaultListener.getLoadCnt());
		System.out.println("Update Operations:			" + DefaultListener.getUpdateCnt());
		System.out.println("Remove Operations:			" + DefaultListener.getRemoveCnt() + "\n");
		System.out.println("Persist Operations:			" + DefaultListener.getPersistCnt());
		System.out.println("Overall time to persist:	" + DefaultListener.getTotalPersistTime()+ "ms");
		System.out.println("Average time to persist:	" + (float) ((float)DefaultListener.getTotalPersistTime()/(float)DefaultListener.getPersistCnt()) +"ms");
	}

	public static void dst04d() {
		System.out.println("=====================");
		System.out.println("========= 4d ========");
		System.out.println("=====================");
		
		SQLInterceptor.resetSelectCount();
		
		dst02b();
		
		System.out.println("Counted select statements for Computers and Executes: " + SQLInterceptor.getSelectCount());
	}

    public static void dst05a() {

		System.out.println("=====================");
		System.out.println("========= 5a ========");
		System.out.println("=====================");
    	
    }

    public static void dst05b() {

		System.out.println("=====================");
		System.out.println("========= 5b ========");
		System.out.println("=====================");
    	
    }

    public static void dst05c() {
    	
		System.out.println("=====================");
		System.out.println("========= 5c ========");
		System.out.println("=====================");

    }
}