package dst3.ejb.util;

import javax.jms.Session;
import javax.persistence.EntityManager;

public class Provider {

	private static Session session;
	private static EntityManager entityManager;

	public static Session getSession() {
		return session;
	}

	public static EntityManager getEntityManager() {
		return entityManager;
	}

	public static void setSession(Session session) {
		Provider.session = session;
	}

	public static void setEntityManager(EntityManager entityManager) {
		Provider.entityManager = entityManager;
	}
}
