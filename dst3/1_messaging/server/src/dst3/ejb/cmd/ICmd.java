package dst3.ejb.cmd;

import javax.jms.MapMessage;

public interface ICmd {

	public void init(MapMessage message) throws CmdException;

	public void exec() throws CmdException;
}
