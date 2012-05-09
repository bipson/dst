package dst2.ejb.interfaces;

import javax.ejb.Remote;

@Remote
public interface TestBeanRemote {
	public void InsertTestEntities();
}
