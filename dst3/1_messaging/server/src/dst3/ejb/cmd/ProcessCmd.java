package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.QueueSender;
import javax.jms.TextMessage;

import dst3.ejb.util.Provider;

public class ProcessCmd extends CmdWithTask implements ICmd {

	QueueSender schedulerQueueSender;

	public ProcessCmd(QueueSender schedulerQueueSender) {
		this.schedulerQueueSender = schedulerQueueSender;
	}

	@Override
	public void exec() throws CmdException {
		Provider.getEntityManager().merge(task);
		Provider.getEntityManager().flush();

		String text = "Finished processing Task " + task.getId();

		try {
			TextMessage message = Provider.getSession().createTextMessage(text);
			schedulerQueueSender.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new CmdException(e.getMessage());
		}

	}

}
