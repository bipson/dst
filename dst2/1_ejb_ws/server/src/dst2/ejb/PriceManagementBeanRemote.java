//TODO delete?
package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Remote;

import dst2.model.PriceStep;

@Remote
public interface PriceManagementBeanRemote {
	public void StorePriceSteps(PriceStep priceStep);

	public BigDecimal RetrieveFee(Integer numberOfJobs);
}
