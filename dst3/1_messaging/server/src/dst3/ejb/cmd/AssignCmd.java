package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;
import javax.jms.TextMessage;

import dst3.ejb.util.Provider;
import dst3.model.Task;
import dst3.model.TaskComplexity;
import dst3.model.TaskStatus;

public class AssignCmd extends CmdWithId {

	QueueSender schedulerQueueSender;
	QueueSender clusterQueueSender;

	public AssignCmd(QueueSender schedulerQueueSender,
			QueueSender clusterQueueSender) {
		this.schedulerQueueSender = schedulerQueueSender;
		this.clusterQueueSender = clusterQueueSender;
	}

	@Override
	public void exec() throws CmdException {
		Task task = new Task();

		task.setJobId(id);
		task.setComplexity(TaskComplexity.UNRATED);
		task.setStatus(TaskStatus.ASSIGNED);

		Provider.getEntityManager().persist(task);
		Provider.getEntityManager().flush();

		try {
			ObjectMessage objMessage = Provider.getSession()
					.createObjectMessage(task.getDTO());
			clusterQueueSender.send(objMessage);
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			TextMessage message = Provider.getSession().createTextMessage(
					task.getId().toString());
			schedulerQueueSender.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new CmdException(e.getMessage());
		}

	}
}
