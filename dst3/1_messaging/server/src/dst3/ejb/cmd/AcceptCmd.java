package dst3.ejb.cmd;

import dst3.ejb.util.Provider;

public class AcceptCmd extends CmdWithTask {

	@Override
	public void exec() throws CmdException {
		Provider.getEntityManager().merge(task);
	}

}
