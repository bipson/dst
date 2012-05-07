package dst2.ejb;

import javax.ejb.Remote;

@Remote
public interface TestBeanRemote {
	public void InsertTestEntities();
}
