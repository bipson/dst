package dst2.ejb;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dst2.model.Execution;
import dst2.model.Job;
import dst2.model.JobStatus;

@Startup
@Singleton
public class TimerService {

	@PersistenceContext
	EntityManager em;

	@Schedule(minute = "*")
	public void executeJobs() {
		// TODO improve query - go over executions?
		Query query = em
				.createQuery("SELECT j FROM Job j JOIN FETCH j.execution");
		@SuppressWarnings("unchecked")
		Collection<Job> col = (Collection<Job>) query.getResultList();
		if (!(col.isEmpty())) {
			for (Job job : col) {
				Execution exec = job.getExecution();
				if (exec.getStart() != null && exec.getEnd() == null) {
					exec.setEnd(new Date());
					exec.setStatus(JobStatus.FINISHED);
					em.persist(em);
					em.flush();
				}
			}
		}
	}
}
