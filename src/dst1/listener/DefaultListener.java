package dst1.listener;

import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.*;

public class DefaultListener {

	private static final ThreadSafeCounter persistCnt = new ThreadSafeCounter();
	private static final ThreadSafeCounter persistTime = new ThreadSafeCounter();
	private static final ThreadSafeCounter loadCnt = new ThreadSafeCounter();
	private static final ThreadSafeCounter updateCnt = new ThreadSafeCounter();
	private static final ThreadSafeCounter removeCnt = new ThreadSafeCounter();

	private static ConcurrentHashMap<Object, Long> persistMap = new ConcurrentHashMap<Object, Long>();
	
	@PrePersist
	public void prePersist(Object entity) {
		persistMap.putIfAbsent(entity, (Long) (System.currentTimeMillis()));
	}
	
	@PostPersist
	public void postPersist(Object entity) {
		persistCnt.increaseMe();
		if (persistMap.get(entity) != null)
			persistTime.add((int) (System.currentTimeMillis() - persistMap.get(entity)));
	}
	
	@PostLoad
	public void postLoad(Object entity) {
		loadCnt.increaseMe();
	}
	
	@PostUpdate
	public void postUpdate(Object entity) {
		updateCnt.increaseMe();
	}
	
	@PostRemove
	public void postRemove(Object entity) {
		removeCnt.increaseMe();
	}
	
	public static int getPersistCnt() {
		return persistCnt.getMe();
	}
	
	public static long getTotalPersistTime() {
		return persistTime.getMe();
	}

	public static int getLoadCnt() {
		return loadCnt.getMe();
	}

	public static int getUpdateCnt() {
		return updateCnt.getMe();
	}

	public static int getRemoveCnt() {
		return removeCnt.getMe();
	}
	
	
}
