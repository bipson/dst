package dst3.ejb.cmd;

public class AssignCmd extends CmdWithId {

	@Override
	public void exec() {
		System.out.println("exec : " + id);
	}

}
