package dst1.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.HibernateEntityManager;

import dst1.db.GenericDao;
import dst1.model.Execution;
import dst1.model.Job;
import dst1.model.JobStatus;

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
		// TODO: make javadoc of this
		// Note: The query does only check Job and associated Execution (if any)
		// not less and not more and fixed JobStatus in Execution (namely
		// JobStatus.FINISHED (will be overwritten) as requested by assignment
		// This is obviously DANGEROUS (how should user know?)
		HibernateEntityManager hem = GenericDao.getEntityManager().unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		
		Execution exec = null;
		if (job.getExecution() != null)
			exec = job.getExecution();
		else
			exec = new Execution();
		
		exec.setStatus(JobStatus.FINISHED);
		
		Criteria criteria = session.createCriteria(Job.class)
		.add(Example.create(job))
		
		.createCriteria("execution")
			.add( Example.create(exec) );
		return (criteria.list());
	}
}
