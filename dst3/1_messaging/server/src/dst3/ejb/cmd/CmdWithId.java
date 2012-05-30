package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.MapMessage;

public abstract class CmdWithId implements ICmd {

	protected Long id;

	@Override
	public void init(MapMessage message) throws CmdException {
		try {
			id = message.getLong("taskid");
		} catch (NumberFormatException e) {
			throw new CmdException("Task id not specified");
		} catch (JMSException e) {
			throw new CmdException(e.getErrorCode());
		}
	}

}
