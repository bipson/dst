package dst3.eventing;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import dst3.DTO.TaskDTO;

public class EventProcessor {

	// public class AssignedListener implements UpdateListener {
	// EPRuntime cepRT;
	//
	// AssignedListener(EPRuntime cepRT) {
	// this.cepRT = cepRT;
	// }
	//
	// @Override
	// public void update(EventBean[] newData, EventBean[] oldData) {
	// cepRT.sendEvent(TaskAssigned);
	// }
	// }

	// private static final String ASSIGNED_QUERY =
	// "select * from Task(TaskStatus=ASSIGNED)";
	// private static final String PROCESSED_QUERY =
	// "select * from Task(TaskStatus=PROCESSED)";
	// private static final String DURATION_QUERY =
	// "select * from Task(TaskStatus=ASSIGNED)";

	private static final String ASSIGNED_SCHEMA_QUERY = "create schema TaskAssigned as (jobId long, timeStamp long)";
	private static final String PROCESSED_SCHEMA_QUERY = "create schema TaskProcessed as (jobId long, timeStamp long)";
	private static final String DURATION_SCHEMA_QUERY = "create schema TaskDuration as (jobId long, duration long)";

	private static final String INSERT_ASSIGNED = "insert into TaskAssigned select T.jobId as jobId, current_timestamp as timeStamp from Task T where T.status = dst3.model.TaskStatus.ASSIGNED";
	private static final String INSERT_PROCESSED = "insert into TaskProcessed select T.jobId as jobId, current_timestamp as timeStamp from Task T where T.status = dst3.model.TaskStatus.PROCESSED";
	private static final String INSERT_DURATION = "insert into TaskDuration select TA.jobId as jobId, TP.timeStamp - TA.timeStamp as duration from TaskAssigned.win:length(10000) TA, TaskProcessed.win:length(10000) TP where TA.jobId = TP.jobId";

	public static void main(String args[]) {
		// The Configuration is meant only as an initialization-time object.
		Configuration cepConfig = new Configuration();
		// We register Ticks as objects the engine will have to handle
		cepConfig.addEventType("Task", TaskDTO.class.getName());

		// We setup the engine
		EPServiceProvider cep = EPServiceProviderManager.getProvider(
				"EsperEngineDST", cepConfig);

		EPRuntime cepRT = cep.getEPRuntime();

		// We register an EPL statement
		EPAdministrator cepAdm = cep.getEPAdministrator();

		EPStatement assignedStatement = cepAdm.createEPL(ASSIGNED_SCHEMA_QUERY);
		EPStatement processedStatement = cepAdm
				.createEPL(PROCESSED_SCHEMA_QUERY);
		EPStatement durationStatement = cepAdm.createEPL(DURATION_SCHEMA_QUERY);

		assignedStatement.start();
		processedStatement.start();
		durationStatement.start();

		EPStatement insertAssigned = cepAdm.createEPL(INSERT_ASSIGNED);
		EPStatement insertProcessed = cepAdm.createEPL(INSERT_PROCESSED);
		EPStatement insertDuration = cepAdm.createEPL(INSERT_DURATION);

		insertAssigned.start();
		insertProcessed.start();
		insertDuration.start();

		// EPStatement assignedQueryStatement =
		// cepAdm.createEPL(ASSIGNED_QUERY);
		//
		// assignedQueryStatement.addListener(new AssignedListener());

		// EPStatement cepStatement = cepAdm.createEPL("select * from "
		// + "TaskDTO(symbol='AAPL').win:length(2) "
		// + "having avg(price) > 6.0");

	}
}
