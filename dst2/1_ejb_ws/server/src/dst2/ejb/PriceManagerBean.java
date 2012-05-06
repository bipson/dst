package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.model.PriceStep;

@Startup
@Singleton
public class PriceManagerBean implements PriceManagerBeanRemote {

	@PersistenceContext
	EntityManager em;

	ArrayList<PriceStep> priceStepCache = new ArrayList<PriceStep>();

	@SuppressWarnings("unchecked")
	@PostConstruct
	void init() {
		Query query = em.createQuery("SELECT p FROM PriceStep e");
		priceStepCache.addAll((Collection<? extends PriceStep>) query
				.getResultList());
	}

	@Override
	public void StorePriceSteps(PriceStep priceStep) {
		priceStepCache.add(priceStep);
		Collections.sort(priceStepCache);
		em.persist(priceStep);
	}

	@Override
	public BigDecimal RetrieveFee(Integer numberOfJobs) {
		if (priceStepCache.isEmpty())
			return BigDecimal.ZERO;
		if (priceStepCache.get(priceStepCache.size() - 1)
				.getNumberOfHistoricalJobs() < numberOfJobs)
			return BigDecimal.ZERO;
		if (priceStepCache.get(0).getNumberOfHistoricalJobs() < numberOfJobs)
			return priceStepCache.get(0).getPrice();

		// Binary Search for biggest element where numberofJobs <=
		// input.numberOfJobs
		int upperLimit = priceStepCache.size() - 1;
		int lowerLimit = 0;
		int pos = upperLimit / 2;
		PriceStep current, next;
		do {
			current = priceStepCache.get(pos);
			next = priceStepCache.get(pos + 1);
			if (current.getNumberOfHistoricalJobs() > numberOfJobs)
				upperLimit = pos;
			else if (next.getNumberOfHistoricalJobs() < numberOfJobs)
				lowerLimit = pos;
			else
				break;
			pos = ((upperLimit + lowerLimit) / 2);

		} while (true);

		return current.getPrice();
	}
}
