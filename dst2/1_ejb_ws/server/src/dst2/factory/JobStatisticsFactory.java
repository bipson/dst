package dst2.factory;

import java.util.List;

import dst2.DTO.ExecutionDTO;
import dst2.DTO.JobStatisticsDTO;
import dst2.model.Computer;
import dst2.model.Execution;

public class JobStatisticsFactory {

	private static JobStatisticsFactory instance = null;

	private JobStatisticsFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static JobStatisticsFactory getInstance() {
		if (instance == null) {
			instance = new JobStatisticsFactory();
		}
		return instance;
	}

	public JobStatisticsDTO createDTO(String gridname,
			List<Execution> executions) {

		JobStatisticsDTO jobStatDTO = new JobStatisticsDTO();

		jobStatDTO.setGridname(gridname);

		for (Execution exec : executions) {
			ExecutionDTO execDTO = new ExecutionDTO();

			execDTO.setEnd(exec.getEnd());
			execDTO.setStart(exec.getStart());
			int cpuSum = 0;
			for (Computer comp : exec.getComputerList()) {

				cpuSum += comp.getCpus();

			}
			execDTO.setNumCPUs(cpuSum);
		}

		return jobStatDTO;
	}
}
