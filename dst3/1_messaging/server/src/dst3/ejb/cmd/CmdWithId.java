package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

public abstract class CmdWithId implements ICmd {

	protected Long id;

	@Override
	public void init(Message message) throws CmdException {
		if (message instanceof MapMessage) {
			try {
				MapMessage mapMessage = (MapMessage) message;
				id = mapMessage.getLong("taskid");
			} catch (NumberFormatException e) {
				throw new CmdException("Task id not specified");
			} catch (JMSException e) {
				throw new CmdException(e.getErrorCode());
			}
		} else {
			throw new CmdException("Wrong Message type received!");
		}
	}

}
