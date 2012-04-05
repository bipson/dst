package dst1.db;

import dst1.model.Admin;

public class AdminDao extends GenericDao<Admin, Long> {
	
	public AdminDao() {
		super(Admin.class);
	}
	
}
