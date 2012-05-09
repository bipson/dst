package dst2.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.DTO.JobStatisticsDTO;
import dst2.factory.JobStatisticsFactory;
import dst2.model.Execution;

@Stateless
public class JobStatisticsBean {

	@PersistenceContext
	EntityManager em;

	public JobStatisticsDTO getJobStatistics(String gridname) {
		TypedQuery<Execution> query = em
				.createQuery(
						"SELECT e FROM Execution e JOIN FETCH e.computerList JOIN e.computerList comp JOIN comp.cluster clust JOIN clust.grid WHERE grid.name = :gridname AND e.JobStatus = 'FINISHED'",
						Execution.class).setParameter("gridname", gridname);

		JobStatisticsDTO jobStats = JobStatisticsFactory.getInstance()
				.createDTO(gridname, query.getResultList());

		return jobStats;
	}
}
