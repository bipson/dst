package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dst2.model.PriceStep;

@Stateless
public class GeneralManagementBean implements GeneralManagementBeanRemote {

	@EJB
	PriceManagerBean priceManagerBean;

	@Override
	public void setPrice(Integer nrJobs, BigDecimal price) {

		priceManagerBean.StorePriceSteps(new PriceStep(nrJobs, price));
	}
}
