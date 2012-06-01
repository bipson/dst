package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;

import dst3.ejb.util.Provider;
import dst3.model.Task;

public class InfoCmd extends CmdWithId {

	QueueSender queueSender;

	public InfoCmd(QueueSender queueSender) {
		this.queueSender = queueSender;
	}

	@Override
	public void exec() throws CmdException {

		Task task = Provider.getEntityManager().find(Task.class, id);

		try {
			ObjectMessage message = Provider.getSession()
					.createObjectMessage(
							task != null ? task.getDTO() : new String(
									"No Task found!"));
			queueSender.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new CmdException(e.getMessage());
		}

	}
}
