package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.TopicPublisher;

import dst3.ejb.util.Provider;

public class AcceptCmd extends CmdWithTask {

	TopicPublisher computerPublisher;

	public AcceptCmd(TopicPublisher computerPublisher) {
		this.computerPublisher = computerPublisher;
	}

	@Override
	public void exec() throws CmdException {
		Provider.getEntityManager().merge(task);
		Provider.getEntityManager().flush();

		try {
			ObjectMessage objMessage = Provider.getSession()
					.createObjectMessage(task.getDTO());
			objMessage.setObjectProperty("Comb",
					task.getRatedBy() + ":" + task.getComplexity());
			System.out.println("Comb = '" + task.getRatedBy() + ":"
					+ task.getComplexity() + "'");
			computerPublisher.publish(objMessage);
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
