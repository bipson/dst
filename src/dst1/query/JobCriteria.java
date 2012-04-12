package dst1.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.HibernateEntityManager;

import dst1.db.GenericDao;
import dst1.model.Job;

public class JobCriteria {

	@SuppressWarnings("rawtypes")
	public static List byUserWorkflow(String user, String workflow) {
		HibernateEntityManager hem = GenericDao.getEntityManager().unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		
		Criteria criteria = session.createCriteria(Job.class);
		criteria.createAlias("user", "u");
		criteria.createAlias("environment", "e");
		criteria.add(Restrictions.eq("u.username", user));
		criteria.add(Restrictions.eq("e.workflow", workflow));
		return (criteria.list());
	}
	
	@SuppressWarnings("rawtypes")
	public static List byExample(Job job) {
		HibernateEntityManager hem = GenericDao.getEntityManager().unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		
		Example jobExample = Example.create(job);
		jobExample.excludeProperty("paid");
		
		Criteria criteria = session.createCriteria(Job.class);
		criteria.createAlias("execution", "ex");
		criteria.add(jobExample);
		return (criteria.list());
	}
}
