package dst2.ejb;

import java.math.BigDecimal;
import java.util.concurrent.Future;

import javax.ejb.Remote;

@Remote
public interface GeneralManagementBeanRemote {
	public void setPrice(Integer nrJobs, BigDecimal price);

	public Future<String> getBill(String username);
}
