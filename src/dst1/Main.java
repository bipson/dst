package dst1;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.management.timer.Timer;

import dst1.db.*;
import dst1.model.*;

public class Main {
	
	private Main() {
		super();
	}

	public static void main(String[] args) {
		
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
	}

	public static void dst01() {
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
		// Environments
		Environment env1 = new Environment("abcd", Arrays.asList("abc", "cde"));
		Environment env2 = new Environment("efghi", Arrays.asList("efg", "hij"));
		
		environmentDao.persist(env1);
		environmentDao.persist(env2);
		
		// Users
		User user1 = null, user2 = null;
		try {
			user1 = new User("gacksi", "foo1".getBytes("UTF-8"), "123", "8000");
			user2 = new User("quacksi", "foo2".getBytes("UTF-8"), "12345", "8000");
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
		Computer computer1 = new Computer("comp1", 6, "cellar1", new Date(), new Date());
		Computer computer2 = new Computer("comp2", 6, "cellar1", new Date(), new Date());
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
				new Date(), new Date(now + 3L * Timer.ONE_WEEK), JobStatus.RUNNING);
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
		
		userDao.persist(user1);
		
		executionDao.persist(exec1);
		executionDao.persist(exec2);
		
		computer1.getExecutionList().add(exec1);
		computer2.getExecutionList().add(exec1);
		computer3.getExecutionList().add(exec1);
		computer4.getExecutionList().add(exec1);

		computer3.getExecutionList().add(exec2);
		computer4.getExecutionList().add(exec2);
		
		System.out.println("Added Test Entities to Database");
		
		// Retrieve some of the Test-Entities
		
		System.out.println("Retrieving some Test Entities");
		
		User testUser = userDao.get(user1.getId());
		System.out.println("testUser: " +testUser.toString());
		System.out.println("testUser-address: " +testUser.getAddress().toString());
		System.out.println("testUser-memberships: " +testUser.getMembershipList());
		System.out.println("testUser-jobs: " +testUser.getJobList());
		System.out.println("jumping to grid in first membership in list...");
		Membership testMembership = (Membership)(testUser.getMembershipList().toArray())[0];
		Grid testGrid = testMembership.getGrid();
		System.out.println("testuser-grid: " +testGrid.toString());
		System.out.println("testuser-grid-membership: " +testGrid.getMembershipList().toString());
		System.out.println("testuser-grid-Clusters: " +testGrid.getClusterList().toString());
		System.out.println("jumping to first cluster in cluster-list...");
		Cluster testCluster = (Cluster)(testGrid.getClusterList().toArray()[0]);
		System.out.println("testcluster: " +testCluster.toString());
		System.out.println("testcluster-computers: " +testCluster.getComputerList());
		System.out.println("testcluster-grids: " +testCluster.getGrid().toString());
		System.out.println("testcluster-admin: " +testCluster.getAdmin().toString());
		System.out.println("testcluster-cluster_children: " +testCluster.getClusterChildren().toString());
		System.out.println("jumping to first computer in computer-list...");
		Computer testComputer = (Computer)(testCluster.getComputerList().toArray()[0]);
		System.out.println("testComputer: " +testComputer.toString());
		System.out.println("testComputer-clusters: " +testComputer.getCluster());
		System.out.println("jumping to first exeuction in execution-list...");
		Execution testExecution = (Execution)(testComputer.getExecutionList().toArray()[0]);
		System.out.println("testExecution: " +testExecution.toString());
		System.out.println("testExecution-job: " +testExecution.getJob().toString());
		System.out.println("testExecution-computers: " +testExecution.getComputerList().toString());
		System.out.println("jumping to first job in job-list...");
		Job testJob = (Job)(testExecution.getJob());
		System.out.println("testJob: " +testJob.toString());
		System.out.println("testJob-user: " +testJob.getUser().toString());
		System.out.println("testJob-exec: " +testJob.getExecution().toString());
		System.out.println("testJob-env: " +testJob.getEnvironment().toString());
		
		System.out.println("jumping to first job in list...");
//		Job testjob = user1.getJobList();
//		System.out.println("job: " +testjob.toString());
//		System.out.println("job-env: " +testjob.getEnvironment().toString());
		
		Admin testAdmin = adminDao.get(admin1.getId());
		System.out.println("testAdmin: " +testAdmin.toString());
		System.out.println("testAdmin-clusters: " +testAdmin.getClusterList());
		
		Cluster testCluster2 = clusterDao.get(cluster1.getId());
	}

	public static void dst02a() {

	}

	public static void dst02b() {

	}

	public static void dst02c() {

	}

	public static void dst03() {

	}

	public static void dst04a() {

	}

	public static void dst04b() {

	}

	public static void dst04c() {

	}

	public static void dst04d() {

	}

    public static void dst05a() {

    }

    public static void dst05b() {

    }

    public static void dst05c() {

    }
}
