package dst2.ejb.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.Remote;

import dst2.DTO.AuditLogDTO;

@Remote
public interface GeneralManagementBeanRemote {
	public void setPrice(Integer nrJobs, BigDecimal price);

	public Future<String> getBill(String username);

	public List<AuditLogDTO> getAuditLog();
}
