package dst2.ejb;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.management.timer.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst2.ejb.interfaces.TestBeanRemote;
import dst2.model.Address;
import dst2.model.Admin;
import dst2.model.Cluster;
import dst2.model.Computer;
import dst2.model.Environment;
import dst2.model.Execution;
import dst2.model.Grid;
import dst2.model.Job;
import dst2.model.JobStatus;
import dst2.model.Membership;
import dst2.model.User;

@Stateless
public class TestBean implements TestBeanRemote {

	@PersistenceContext
	EntityManager em;

	@Remove
	public void InsertTestEntities() {

		// Add some Test-entities
		System.out.println("Will now add some Test-Entities");

		// Environments
		Environment env1 = new Environment("abcd", new LinkedList<String>(
				Arrays.asList("abc", "cde")));
		em.persist(env1);

		em.flush();

		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// Users
		User user1 = null, user2 = null;
		try {
			user1 = new User("gacksi", md.digest("foo1".getBytes("UTF-8")),
					"123", "8000");
			user2 = new User("quacksi", md.digest("foo2".getBytes("UTF-8")),
					"12345", "8000");
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

		em.persist(user1);
		em.flush();

		em.persist(user2);
		em.flush();

		// Grids ( + Memberships)
		Grid grid1 = new Grid("grid1", "cellar1", BigDecimal.valueOf(31482));
		Grid grid2 = new Grid("grid2", "cellar2", BigDecimal.valueOf(32554));

		em.persist(grid1);
		em.flush();

		em.persist(grid2);
		em.flush();

		Membership membership1 = new Membership(new Date(),
				(Double.valueOf(23)));

		membership1.setGrid(grid1);
		membership1.setUser(user1);

		user1.getMembershipList().add(membership1);
		grid1.getMembershipList().add(membership1);

		Membership membership2 = new Membership(new Date(
				System.currentTimeMillis() - (5L * Timer.ONE_WEEK)),
				(Double.valueOf(36)));

		membership2.setGrid(grid2);
		membership2.setUser(user2);

		user2.getMembershipList().add(membership2);
		grid2.getMembershipList().add(membership2);

		em.persist(membership1);
		em.persist(membership2);

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

		em.persist(admin1);
		em.flush();

		em.persist(admin2);
		em.flush();

		// Clusters
		Cluster cluster1 = null, cluster2 = null;

		cluster1 = new Cluster("Clust1", new Date(System.currentTimeMillis()
				- (8L * 52L * Timer.ONE_WEEK)), new Date(
				System.currentTimeMillis() + (3L * 52L * Timer.ONE_WEEK)));

		cluster1.setAdmin(admin1);
		cluster1.setGrid(grid1);

		grid1.getClusterList().add(cluster1);
		admin1.getClusterList().add(cluster1);

		em.persist(cluster1);
		em.flush();

		cluster2 = new Cluster("Clust2", new Date(System.currentTimeMillis()
				- (6L * 52L * Timer.ONE_WEEK)), new Date(
				System.currentTimeMillis() + (5L * 52L * Timer.ONE_WEEK)));

		cluster2.setAdmin(admin2);
		cluster2.setGrid(grid2);

		grid2.getClusterList().add(cluster2);
		admin2.getClusterList().add(cluster2);

		em.persist(cluster2);
		em.flush();

		// Computers
		Computer computer1 = new Computer("comp1", 6, "AUT-VIE", new Date(),
				new Date());
		Computer computer2 = new Computer("comp2", 6, "AUT-VIE", new Date(),
				new Date());
		Computer computer3 = new Computer("comp3", 2, "AUT-VIE", new Date(),
				new Date());
		Computer computer4 = new Computer("comp4", 2, "AUT-VIE", new Date(),
				new Date());
		Computer computer5 = new Computer("comp5", 1, "AUT-VIE", new Date(),
				new Date());

		computer1.setCluster(cluster1);
		computer2.setCluster(cluster1);
		computer3.setCluster(cluster2);
		computer4.setCluster(cluster2);
		computer5.setCluster(cluster1);

		em.persist(computer1);
		em.flush();

		em.persist(computer2);
		em.flush();

		em.persist(computer3);
		em.flush();

		em.persist(computer4);
		em.flush();

		em.persist(computer5);
		em.flush();

		cluster1.getComputerList().add(computer1);
		cluster1.getComputerList().add(computer2);
		cluster2.getComputerList().add(computer3);
		cluster2.getComputerList().add(computer4);
		cluster1.getComputerList().add(computer5);

		em.merge(cluster1);
		em.merge(cluster2);

		// Jobs
		Job job1 = new Job(true);

		job1.setEnvironment(env1);
		job1.setUser(user1);

		em.persist(job1);

		user1.getJobList().add(job1);

		// Executions
		Execution exec1 = new Execution(new Date(System.currentTimeMillis()
				- (30L * Timer.ONE_MINUTE)), null, JobStatus.RUNNING);

		exec1.setJob(job1);
		exec1.getComputerList().add(computer1);

		em.persist(exec1);
		em.flush();

		job1.setExecution(exec1);
		computer1.getExecutionList().add(exec1);

		// TODO redundant?
		em.merge(job1);
		em.merge(user1);
		em.merge(computer1);

		em.flush();

		System.out.println("Added Test Entities to Database");
	}
}
