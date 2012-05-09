package dst2.webservice;

import javax.jws.WebService;

import dst2.DTO.JobStatisticsDTO;
import dst2.webservice.exception.UnknownGridFault;

@WebService
public interface JobStatisticsInterface {

	public JobStatisticsDTO getStatistics(String gridname)
			throws UnknownGridFault;
}
