package dst2.webservice;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.soap.Addressing;

import dst2.DTO.JobStatisticsDTO;
import dst2.ejb.JobStatisticsBean;
import dst2.webservice.exception.UnknownGridFault;

@Stateless
@Addressing(required = true)
@WebService(endpointInterface = "dst2.webservice.JobStatistics", name = "service", serviceName = "JobStatistics")
public class JobStatistics implements JobStatisticsInterface {

	@EJB
	JobStatisticsBean jobStatsBean;

	@WebMethod
	@Action(input = "localhost:8080/JobStatistics/service/input", output = "localhost:8080/JobStatistics/service/output", fault = { @FaultAction(className = UnknownGridFault.class, value = "localhost:8080/JobStatistics/service/fault") })
	@SOAPBinding(parameterStyle = ParameterStyle.BARE)
	public @WebResult(header = false)
	JobStatisticsDTO getStatistics(
			@WebParam(name = "gridname", header = true, mode = Mode.IN) String gridname)
			throws UnknownGridFault {
		return jobStatsBean.getJobStatistics(gridname);
	}
}
