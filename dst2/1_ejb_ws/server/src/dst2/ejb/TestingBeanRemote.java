package dst2.ejb;

import javax.ejb.Remote;

@Remote
public interface TestingBeanRemote {
	public void InsertTestEntities();
}
