package dst1.db;

import dst1.model.Computer;

public class ComputerDao extends GenericDao<Computer, Long> {
	
	public ComputerDao() {
		super(Computer.class);
	}
	
}
