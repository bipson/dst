package dst2.ejb;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import dst2.model.Execution;
import dst2.model.JobStatus;

@Startup
@Singleton
public class TimerService {

	@PersistenceContext
	EntityManager em;

	@Schedule(second = "42", minute = "*/1", hour = "*")
	public void executeJobs() {
		// TODO improve query - go over executions?
		TypedQuery<Execution> query = em.createQuery(
				"SELECT e FROM Execution e", Execution.class);
		Collection<Execution> col = query.getResultList();
		for (Execution exec : col) {
			if (exec != null) {
				if (exec.getStart() != null && exec.getEnd() == null) {
					exec.setEnd(new Date());
					exec.setStatus(JobStatus.FINISHED);
					em.merge(exec);
					em.flush();
				}
			}
		}
	}
}
