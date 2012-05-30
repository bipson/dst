package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.QueueSender;

import dst3.ejb.util.Provider;

public class InfoCmd extends CmdWithId {

	QueueSender queueSender;

	public InfoCmd(QueueSender queueSender) {
		this.queueSender = queueSender;
	}

	@Override
	public void exec() throws CmdException {
		// TODO get Task from db
		
		System.out.println("received : "+id);

		try {
			ObjectMessage message = Provider.getSession().createObjectMessage(
					id.toString());
			queueSender.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			throw new CmdException(e.getMessage());
		}

	}
}
