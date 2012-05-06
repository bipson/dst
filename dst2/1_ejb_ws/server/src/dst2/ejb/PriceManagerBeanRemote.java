package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Remote;

import dst2.model.PriceStep;

@Remote
public interface PriceManagerBeanRemote {
	public void StorePriceSteps(PriceStep priceStep);

	public BigDecimal RetrieveFee(Integer numberOfJobs);
}
