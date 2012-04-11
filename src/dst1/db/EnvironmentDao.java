package dst1.db;

import dst1.model.Environment;

public class EnvironmentDao extends GenericDao<Environment, Long> {
	
	public EnvironmentDao() {
		super(Environment.class);
	}
	
}
