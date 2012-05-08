package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.management.timer.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.DTO.AuditLogDTO;
import dst2.model.AuditLog;
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

		String bill = "Nothing to bill :)\n";

		List<CostsPerJob> costsPerJob = new ArrayList<CostsPerJob>();
		Integer numberOfJobs = 0;

		TypedQuery<User> query = em
				.createQuery(
						"SELECT u FROM User u WHERE u.username = :username",
						User.class).setParameter("username", username);

		User user;

		try {
			user = query.getSingleResult();
		} catch (RuntimeException e) {
			return new AsyncResult<String>("Not a known user\n");
		}

		for (Job job : user.getJobList()) {
			if (job.getExecution().getStatus() == JobStatus.FINISHED
					&& job.isPaid())
				numberOfJobs++;
		}

		for (Job job : user.getJobList()) {
			if (job.getExecution().getStatus() == JobStatus.FINISHED
					&& !job.isPaid()) {

				CostsPerJob tempCPJ = new CostsPerJob();

				// Add execution costs
				for (Computer comp : job.getExecution().getComputerList()) {

					if (comp.getCluster() == null) {
						return new AsyncResult<String>("Cluster null!\n");
					}

					if (comp.getCluster().getGrid() == null) {
						return new AsyncResult<String>("Grid null!\n");
					}

					BigDecimal costsPerMinute = comp.getCluster().getGrid()
							.getCostsPerCPUMinute();

					Integer numCPUs = comp.getCpus();

					// TODO simplify
					tempCPJ.executionCosts = tempCPJ.executionCosts
							.add(costsPerMinute.multiply(
									new BigDecimal(numCPUs)).multiply(
									new BigDecimal(job.getExecutionTime()
											/ Timer.ONE_MINUTE)));

					for (Membership membership : user.getMembershipList()) {

						if (comp.getCluster().getGrid()
								.equals(membership.getGrid())) {
							tempCPJ.executionCosts = tempCPJ.executionCosts
									.subtract(new BigDecimal(membership
											.getDiscount()));
						}
					}
					tempCPJ.computerCount++;
				}

				// Add setup costs
				tempCPJ.setupCosts = tempCPJ.setupCosts.add(priceManagerBean
						.RetrieveFee(numberOfJobs));

				costsPerJob.add(tempCPJ);

				job.setPaid(true);
				em.merge(job);
				numberOfJobs++;
			}
			em.flush();
		}

		if (!costsPerJob.isEmpty()) {

			bill = "--- Bill for user: " + username + " ---\n";

			// TODO total price, price per job, setup costs, execution costs,
			// computers per job
			BigDecimal totalCosts = BigDecimal.ZERO;
			BigDecimal executionCosts = BigDecimal.ZERO;
			BigDecimal setupCosts = BigDecimal.ZERO;

			for (CostsPerJob cpj : costsPerJob) {

				bill += ("Job on " + cpj.computerCount + " computers\n");
				bill += ("Setup costs: " + cpj.setupCosts + "\n");
				bill += ("Execution costs: " + cpj.executionCosts + "\n");
				bill += ("Costs for this Job : "
						+ cpj.setupCosts.add(cpj.executionCosts) + "\n");
				bill += (" . . . . . . . . . . . . . . . .\n");

				executionCosts = executionCosts.add(cpj.executionCosts);
				setupCosts = setupCosts.add(cpj.setupCosts);
				totalCosts = totalCosts.add(cpj.executionCosts
						.add(cpj.setupCosts));
			}

			bill += ("=-=-= Bill Sum =-=-=-=-=-=-=-=-\n");
			bill += ("Setup Costs overall: " + setupCosts + "\n");
			bill += ("Execution Costs overall: " + executionCosts + "\n");
			bill += ("Overall Costs: --> " + totalCosts + " <--\n");

		}

		return new AsyncResult<String>(bill);
	}

	public class CostsPerJob {
		BigDecimal setupCosts = BigDecimal.ZERO;
		BigDecimal executionCosts = BigDecimal.ZERO;
		Integer computerCount = 0;
	}

	@Override
	public List<AuditLogDTO> getAuditLog() {
		List<AuditLogDTO> logList = new ArrayList<AuditLogDTO>();

		TypedQuery<AuditLog> query = em.createQuery(
				"SELECT a FROM AuditLog a JOIN FETCH a.params", AuditLog.class);

		for (AuditLog audit : query.getResultList()) {
			logList.add(audit.getDTO());
		}

		Collections.sort(logList);

		return logList;
	}
}
