package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.AsyncResult;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.model.Computer;
import dst2.model.Job;
import dst2.model.JobStatus;
import dst2.model.Membership;
import dst2.model.PriceStep;
import dst2.model.User;

@Stateless
public class GeneralManagementBean implements GeneralManagementBeanRemote {

	@EJB
	PriceManagementBean priceManagerBean;

	@PersistenceContext
	EntityManager em;

	@Override
	public void setPrice(Integer nrJobs, BigDecimal price) {

		priceManagerBean.StorePriceSteps(new PriceStep(nrJobs, price));
	}

	@Override
	@Asynchronous
	public Future<String> getBill(String username) {

		String bill = "Nothing to bill :)";

		List<CostsPerJob> costsPerJob = new ArrayList<CostsPerJob>();
		Integer numberOfJobs = 0;

		Query query = em
				.createQuery("SELECT u FROM User u JOIN FETCH u.jobList");
		User user = (User) query.getResultList().get(0);

		for (Job job : user.getJobList()) {
			if (job.getExecution().getStatus() == JobStatus.FINISHED
					&& job.isPaid())
				numberOfJobs++;
		}

		for (Job job : user.getJobList()) {
			if (job.getExecution().getStatus() == JobStatus.FINISHED
					&& job.isPaid() == false) {

				CostsPerJob tempCPJ = new CostsPerJob();

				// Add execution costs
				for (Computer comp : job.getExecution().getComputerList()) {

					BigDecimal costsPerMinute = comp.getCluster().getGrid()
							.getCostsPerCPUMinute();

					Integer numCPUs = comp.getCpus();

					tempCPJ.executionCosts.add(costsPerMinute.multiply(
							new BigDecimal(numCPUs)).multiply(
							new BigDecimal(job.getExecutionTime())));

					for (Membership membership : user.getMembershipList()) {
						if (comp.getCluster().getGrid()
								.equals(membership.getGrid())) {
							tempCPJ.executionCosts.subtract(new BigDecimal(
									membership.getDiscount()));
						}
					}
					tempCPJ.computerCount++;
				}

				// Add setup costs
				tempCPJ.setupCosts.add(priceManagerBean
						.RetrieveFee(numberOfJobs));

				costsPerJob.add(tempCPJ);

			}
			job.setPaid(true);
		}

		if (!costsPerJob.isEmpty()) {

			bill = "--- Your bill ---\n";

			// TODO total price, price per job, setup costs, execution costs,
			// computers per job
			BigDecimal totalCosts = BigDecimal.ZERO;
			BigDecimal executionCosts = BigDecimal.ZERO;
			BigDecimal setupCosts = BigDecimal.ZERO;

			for (CostsPerJob cpj : costsPerJob) {

				bill.concat("Job on " + cpj.computerCount + " computers\n");
				bill.concat("Setup costs: " + cpj.setupCosts + "\n");
				bill.concat("Execution costs: " + cpj.executionCosts + "\n");
				bill.concat("Costs for this Job : "
						+ cpj.setupCosts.add(cpj.executionCosts) + "\n");

				executionCosts.add(cpj.executionCosts);
				setupCosts.add(cpj.setupCosts);
				totalCosts.add(cpj.executionCosts.add(cpj.setupCosts));
			}

			bill.concat("Your Sum - - - - - - - -\n");
			bill.concat("Setup Costs overall: " + setupCosts + "\n");
			bill.concat("Execution Costs overall: " + executionCosts + "\n");
			bill.concat("Overall Costs: --> " + totalCosts + " <--\n");

		}
		return new AsyncResult<String>(bill);
	}

	public class CostsPerJob {
		BigDecimal setupCosts = BigDecimal.ZERO;
		BigDecimal executionCosts = BigDecimal.ZERO;
		Integer computerCount = 0;
	}

}
