package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Remote;

@Remote
public interface GeneralManagementBeanRemote {
	public void setPrice(Integer nrJobs, BigDecimal price);
}
