package dst1;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.math.BigDecimal;

import javax.management.timer.Timer;

import dst1.db.AdminDao;
import dst1.db.ClusterDao;
import dst1.db.ComputerDao;
import dst1.db.EnvironmentDao;
import dst1.db.GridDao;
import dst1.db.MembershipDao;
import dst1.db.UserDao;
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
		final AdminDao adminDao = new AdminDao();
		final ClusterDao clusterDao = new ClusterDao();
		final ComputerDao computerDao = new ComputerDao();
		final EnvironmentDao environmentDao = new EnvironmentDao();
//		final ExecutionDao executionDao = new ExecutionDao();
		final GridDao gridDao = new GridDao();
		final MembershipDao membershipDao = new MembershipDao();
// 		final JobDao jobDao = new JobDao();
		final UserDao userDao = new UserDao();
		
		// Helpers
		long now = System.currentTimeMillis();
		
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
		Address address1 = new Address("gacksiStreet", "gacksiCity", "12390");
		user1.setAddress(address1);
		
		user2.setFirstName("Alfredus");
		user2.setLastName("Quacksi");
		Address address2 = new Address("quacksiStreet", "quacksiCity", "1234590");
		user2.setAddress(address2);
		
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
		
		Membership membership2 = new Membership(new Date(now - (5L * Timer.ONE_WEEK)), (Double.valueOf(456)));
		
		membership2.setGrid(grid2);
		membership2.setUser(user2);
		
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
		
		cluster2 = new Cluster("Clust2", new Date(now - (6L * 52L * Timer.ONE_WEEK)),
				new Date(now + (5L * 52L * Timer.ONE_WEEK)));
		
		cluster2.setAdmin(admin2);
		cluster2.setGrid(grid2);
		
		clusterDao.persist(cluster1);
		clusterDao.persist(cluster2);
		
		//Cluster Children
		cluster1.getClusterChildren().add(cluster2);
		
		clusterDao.persist(cluster1);
		clusterDao.persist(cluster2);
		
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
