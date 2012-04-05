package dst1.db;

import dst1.model.User;

public class UserDao extends GenericDao<User, Long> {
	
	public UserDao() {
		super(User.class);
	}
	
}
