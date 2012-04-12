package dst1.query;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.ejb.HibernateEntityManager;

import dst1.db.GenericDao;
import dst1.model.Execution;

public class ExecutionCriteria {

	@SuppressWarnings("rawtypes")
	public static List byExample(Execution exec) {
		HibernateEntityManager hem = GenericDao.getEntityManager().unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		
		Example jobExample = Example.create(exec);
		
		Criteria criteria = session.createCriteria(Execution.class);
		criteria.createAlias("execution", "ex");
		criteria.add(jobExample);
		return (criteria.list());
	}
}
