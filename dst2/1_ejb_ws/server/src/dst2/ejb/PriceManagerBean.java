package dst2.ejb;

import java.math.BigDecimal;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst2.ejb.intervalTree.Interval;
import dst2.ejb.intervalTree.IntervalTree;
import dst2.model.PriceStep;

@Startup
@Singleton
public class PriceManagerBean implements PriceManagerBeanRemote {

	@PersistenceContext
	EntityManager em;

	IntervalTree<BigDecimal> intTree = new IntervalTree<BigDecimal>();

	@Override
	public void StorePriceSteps(PriceStep priceStep) {
		Interval<BigDecimal> newInt;
		Interval<BigDecimal> oldInt = (Interval<BigDecimal>) intTree.getIntervals(
				priceStep.getNumberOfHistoricalJobs()).get(0);
		if (oldInt != null) {
			newInt = new Interval<BigDecimal>(
				oldInt.getStart(),
				priceStep.getNumberOfHistoricalJobs(), priceStep.getPrice());

			oldInt.setStart(priceStep.getNumberOfHistoricalJobs() + 1);
		} else {
			Interval<BigDecimal> maxInt = intTree.getMaxInterval();
			newInt = new Interval<BigDecimal>(
					oldInt.getStart(), priceStep.getNumberOfHistoricalJobs(),
					priceStep.getPrice());
		}
		intTree.addInterval(newInt);
		em.persist(priceStep);
	}

	@Override
	public Integer RetrieveFee(Integer numberOfJobs) {
		// TODO Auto-generated method stub
		return null;
	}

}
