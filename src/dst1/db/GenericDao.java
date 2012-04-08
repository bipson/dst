package dst1.db;

import java.io.Serializable;

import javax.persistence.*;
import javax.persistence.metamodel.EntityType;

import dst1.db.interfaces.IEntity;
import dst1.db.interfaces.IEntityDao;

@SuppressWarnings("hiding")
public class GenericDao<EntityType extends IEntity<EntityKeyType>, EntityKeyType extends Serializable> implements IEntityDao<EntityType, EntityKeyType> {

	private static EntityManager entityManager;
	private static EntityManagerFactory entityManagerFactory;

	private Class<EntityType> entityClass;

	public GenericDao(Class<EntityType> entityClass) {
		this.entityClass = entityClass;
		
		if (entityManager == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory("grid");
			entityManager = entityManagerFactory.createEntityManager();
		}
	}

	public static EntityManager getEntityManager() {
		return GenericDao.entityManager;
	}

	@Override
	public void delete(EntityType entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
		}
		catch (RuntimeException runTime) {
			rollBackTransaction();
			throw runTime;
		}
	}

	@Override
	public EntityType get(EntityKeyType key) {
		return entityManager.find(entityClass, key);
	}

	@Override
	public void persist(EntityType entity) {
		entityManager.getTransaction().begin();
		try {
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
		}
		catch(RuntimeException e) {
			rollBackTransaction();
			throw e;
		}
	}

	@Override
	public EntityType update(EntityType entity) {
		EntityType merged;
		entityManager.getTransaction().begin();
		try {
			merged = entityManager.merge(entity);
			entityManager.getTransaction().commit();
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
			entityManager.refresh(entity);
		}
		catch(RuntimeException e) {
			throw e;
		}
	}

	private void rollBackTransaction() {

		if (entityManager.getTransaction().isActive())
			entityManager.getTransaction().rollback();
	}

	public static void shutdown() {
		if(entityManager != null &&
				entityManager.isOpen()) {
			entityManager.close();
		}
		if(entityManagerFactory != null &&
				entityManagerFactory.isOpen()) {
			entityManagerFactory.close();
		}
	}

	
}
