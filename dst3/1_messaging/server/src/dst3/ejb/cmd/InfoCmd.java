package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;

import dst3.ejb.util.Provider;

public class InfoCmd extends CmdWithId {

	QueueSender schedulerQueue;

	public InfoCmd(QueueSender schedulerQueue) {
		this.schedulerQueue = schedulerQueue;
	}

	@Override
	public void exec() throws CmdException {
		// TODO get Task from db

		try {
			ObjectMessage message = Provider.getSession().createObjectMessage(
					id);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new CmdException(e.getMessage());
		}

	}
}
