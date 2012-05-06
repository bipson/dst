package dst2.ejb;

import javax.ejb.Remote;

import dst2.model.PriceStep;

@Remote
public interface PriceManagerBeanRemote {
	public void StorePriceSteps(PriceStep priceStep);

	public Integer RetrieveFee(Integer numberOfJobs);
}
