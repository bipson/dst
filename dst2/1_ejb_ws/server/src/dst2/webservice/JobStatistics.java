package dst2.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import dst2.DTO.JobStatisticsDTO;
import dst2.ejb.JobStatisticsBean;

@Stateless
@WebService(name = "service")
public class JobStatistics {

	@EJB
	JobStatisticsBean jobStatsBean;

	@WebMethod
	public JobStatisticsDTO getStatistics(String gridname) {
		return jobStatsBean.getJobStatistics(gridname);
	}
}
