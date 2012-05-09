package dst2.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.DTO.JobStatisticsDTO;
import dst2.factory.JobStatisticsFactory;
import dst2.model.Execution;
import dst2.model.Grid;
import dst2.webservice.exception.UnknownGridFault;

@Stateless
public class JobStatisticsBean {

	@PersistenceContext
	EntityManager em;

	public JobStatisticsDTO getJobStatistics(String gridname)
			throws UnknownGridFault {
		TypedQuery<Grid> query = em.createQuery(
				"SELECT g FROM Grid g WHERE g.name = :gridname", Grid.class)
				.setParameter("gridname", gridname);

		try {
			query.getSingleResult();
		} catch (RuntimeException e) {
			throw new UnknownGridFault("Grid with name - " + gridname
					+ " - was not found : " + e.getMessage());
		}

		TypedQuery<Execution> query2 = em
				.createQuery(
						"SELECT e FROM Execution e JOIN FETCH e.computerList JOIN e.computerList comp JOIN comp.cluster clust JOIN clust.grid g WHERE g.name = :gridname AND e.status = dst2.model.JobStatus.FINISHED",
						Execution.class).setParameter("gridname", gridname);

		JobStatisticsDTO jobStats = JobStatisticsFactory.getInstance()
				.createDTO(gridname, query2.getResultList());

		return jobStats;
	}
}
