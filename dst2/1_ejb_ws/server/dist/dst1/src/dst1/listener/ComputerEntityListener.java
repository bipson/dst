package dst1.listener;

import java.util.Date;

import javax.persistence.PreUpdate;
import javax.persistence.PrePersist;

import dst1.model.Computer;

public class ComputerEntityListener {	
	
	@PrePersist
	public void prePersist(Computer entity) {
		entity.setLastUpdate(new Date(System.currentTimeMillis()));
		entity.setCreation(new Date(System.currentTimeMillis()));
	}
	
	@PreUpdate
	public void preUpdate(Computer entity) {
		entity.setLastUpdate(new Date(System.currentTimeMillis()));
	}
}
