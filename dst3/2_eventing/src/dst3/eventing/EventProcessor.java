package dst3.eventing;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

import dst3.DTO.TaskDTO;
import dst3.model.TaskComplexity;
import dst3.model.TaskStatus;

public class EventProcessor {

	public static class DurationSchemaListener implements UpdateListener {

		@Override
		public void update(EventBean[] newData, EventBean[] oldData) {

			System.out.println("Received a TaskDuration event: "
					+ newData[0].getUnderlying());
		}
	}

	public static class DurationSchemaListener2 implements UpdateListener {

		@Override
		public void update(EventBean[] newData, EventBean[] oldData) {

			System.out.println("Received a TaskDuration avg-event: "
					+ newData[0].getUnderlying());
		}
	}

	public static class Task3xFailedListener implements UpdateListener {

		@Override
		public void update(EventBean[] newData, EventBean[] oldData) {

			MapEventBean mEB = (MapEventBean) newData[0];

			TaskDTO task = (TaskDTO) mEB.get("failed3");

			System.out.println("Received a 3x failed Task: " + task);
		}
	}

	// private static final String ASSIGNED_QUERY =
	// "select * from Task(TaskStatus=ASSIGNED)";
	// private static final String PROCESSED_QUERY =
	// "select * from Task(TaskStatus=PROCESSED)";

	private static final String DURATION_SCHEMA_QUERY = "select * from TaskDuration";
	private static final String DURATION_SCHEMA_QUERY2 = "select avg(duration) from TaskDuration.win:time(60 sec)";
	private static final String TASK_3FAILED_PATTERN = "every (ready = Task(status=dst3.model.TaskStatus.READY_FOR_PROCESSING) -> failed = Task(jobId = ready.jobId, status=dst3.model.TaskStatus.PROCESSING_NOT_POSSIBLE)"
			+ " -> ready2 = Task(jobId=failed.jobId, status=dst3.model.TaskStatus.READY_FOR_PROCESSING) -> failed2 = Task(jobId=ready2.jobId, status=dst3.model.TaskStatus.PROCESSING_NOT_POSSIBLE)"
			+ " -> ready3 = Task(jobId=failed2.jobId, status=dst3.model.TaskStatus.READY_FOR_PROCESSING) -> failed3 = Task(jobId=ready3.jobId, status=dst3.model.TaskStatus.PROCESSING_NOT_POSSIBLE) )";

	private static final String ASSIGNED_SCHEMA = "create schema TaskAssigned as (jobId long, timeStamp long)";
	private static final String PROCESSED_SCHEMA = "create schema TaskProcessed as (jobId long, timeStamp long)";
	private static final String DURATION_SCHEMA = "create schema TaskDuration as (jobId long, duration long)";

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

		// Create the types
		cepAdm.createEPL(ASSIGNED_SCHEMA);
		cepAdm.createEPL(PROCESSED_SCHEMA);
		cepAdm.createEPL(DURATION_SCHEMA);

		// Create queries to insert these types
		cepAdm.createEPL(INSERT_ASSIGNED);
		cepAdm.createEPL(INSERT_PROCESSED);
		cepAdm.createEPL(INSERT_DURATION);

		// TODO: are these queries now always "active" and executing?

		EPStatement durationSchemaQueryStatement = cepAdm
				.createEPL(DURATION_SCHEMA_QUERY);
		durationSchemaQueryStatement.addListener(new DurationSchemaListener());

		EPStatement durationSchemaQueryStatement2 = cepAdm
				.createEPL(DURATION_SCHEMA_QUERY2);
		durationSchemaQueryStatement2
				.addListener(new DurationSchemaListener2());

		EPStatement task3xFailedQueryStatement = cepAdm
				.createPattern(TASK_3FAILED_PATTERN);
		task3xFailedQueryStatement.addListener(new Task3xFailedListener());

		TaskDTO task1 = assign(cepRT, 1L, 1L); // entry 1
		TaskDTO task2 = assign(cepRT, 2L, 2L); // entry 2

		ready(cepRT, task1);
		TaskDTO task3 = assign(cepRT, 3L, 3L); // entry 3

		wait_msec(14);

		failed(cepRT, task1); // failed 1 1x
		ready(cepRT, task2);
		finished(cepRT, task2); // end 2

		wait_msec(241);

		TaskDTO task4 = assign(cepRT, 4L, 4L); // entry 4

		ready(cepRT, task1);
		ready(cepRT, task3);
		finished(cepRT, task3); // end 3

		wait_msec(13);

		failed(cepRT, task1); // failed 1 2x
		TaskDTO task5 = assign(cepRT, 5L, 5L); // entry 5

		wait_msec(134);

		ready(cepRT, task4);
		finished(cepRT, task4); // end 4
		TaskDTO task6 = assign(cepRT, 6L, 6L); // entry 6

		wait_msec(213);

		ready(cepRT, task6);
		failed(cepRT, task6); // failed 6 1x

		wait_msec(1256);

		ready(cepRT, task5);
		finished(cepRT, task5); // end 5

		TaskDTO task7 = assign(cepRT, 7L, 7L); // entry 7

		wait_msec(145);

		ready(cepRT, task1);
		failed(cepRT, task1); // fail 3x 1

		ready(cepRT, task7);

		ready(cepRT, task6);

		wait_msec(553);

		finished(cepRT, task7); // end 7

		wait_msec(5);

		failed(cepRT, task6); // failed 6 2x

		TaskDTO task8 = assign(cepRT, 8L, 8L); // entry 8

		wait_msec(42);

		ready(cepRT, task6);
		ready(cepRT, task8);

		wait_msec(243);

		finished(cepRT, task6);

		wait_msec(2);

		finished(cepRT, task8);

		TaskDTO task9 = assign(cepRT, 9L, 9L); // entry 9

		ready(cepRT, task1);
		ready(cepRT, task9);

		wait_msec(324);

		failed(cepRT, task9); // failed 9 1x

		wait_msec(23);

		finished(cepRT, task1);

		TaskDTO task10 = assign(cepRT, 10L, 10L); // entry 10

		ready(cepRT, task10);
	}

	private static TaskDTO assign(EPRuntime cepRT, Long jobId, Long taskId) {
		TaskDTO task1 = new TaskDTO(taskId, jobId, TaskStatus.ASSIGNED, "foo",
				TaskComplexity.EASY);
		cepRT.sendEvent(task1);
		return task1;
	}

	private static void failed(EPRuntime cepRT, TaskDTO task) {
		task.setStatus(TaskStatus.PROCESSING_NOT_POSSIBLE);
		cepRT.sendEvent(task);
	}

	private static void finished(EPRuntime cepRT, TaskDTO task) {
		task.setStatus(TaskStatus.PROCESSED);
		cepRT.sendEvent(task); // end 7
	}

	private static void ready(EPRuntime cepRT, TaskDTO task) {
		task.setStatus(TaskStatus.READY_FOR_PROCESSING);
		cepRT.sendEvent(task);
	}

	private static void wait_msec(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
