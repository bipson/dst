package dst2.db;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import dst2.db.interfaces.IEntity;
import dst2.db.interfaces.IEntityDao;

public class GenericDao <EntityType extends IEntity<EntityKeyType>,EntityKeyType extends Serializable> implements IEntityDao<EntityType, EntityKeyType> {

	private static EntityManager em;
	private static EntityManagerFactory emf;

	private Class<EntityType> entityClass;

	public GenericDao(Class<EntityType> entityClass) {
		this.entityClass = entityClass;
	}

	public static void initEMF(EntityManagerFactory emf) {
		GenericDao.emf = emf;
		GenericDao.em = GenericDao.emf.createEntityManager();
	}
	
	public static void setEntityManager(EntityManager em) {
		GenericDao.em = em;
	}

	public static EntityManager getEntityManager() {
		return GenericDao.em;
	}

	@Override
	public void delete(EntityType entity) {
		try {
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
		}
		catch (RuntimeException e) {
			rollBackTransaction();
			throw e;
		}
	}

	@Override
	public EntityType get(EntityKeyType key) {
		return em.find(entityClass, key);
	}

	@Override
	public void persist(EntityType entity) {
		em.getTransaction().begin();
		try {
			em.persist(entity);
			em.getTransaction().commit();
		}
		catch(RuntimeException e) {
			rollBackTransaction();
			throw e;
		}
	}

	@Override
	public EntityType update(EntityType entity) {
		EntityType merged;
		em.getTransaction().begin();
		try {
			merged = em.merge(entity);
			em.getTransaction().commit();
			return merged;
		}
		catch(RuntimeException e) {
			rollBackTransaction();
			throw e;
		}
	}

	@Override
	public void refresh(EntityType entity) {
		try {
			em.refresh(entity);
		}
		catch(RuntimeException e) {
			throw e;
		}
	}

	private void rollBackTransaction() {

		if (em.getTransaction().isActive())
			em.getTransaction().rollback();
	}

	public static void shutdown() {
		if(em != null && em.isOpen()) {
			em.close();
		}
		if(emf != null && emf.isOpen()) {
			emf.close();
		}
	}
}
