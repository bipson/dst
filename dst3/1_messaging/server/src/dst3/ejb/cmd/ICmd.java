package dst3.ejb.cmd;

import javax.jms.Message;

public interface ICmd {

	public void init(Message message) throws CmdException;

	public void exec() throws CmdException;
}
