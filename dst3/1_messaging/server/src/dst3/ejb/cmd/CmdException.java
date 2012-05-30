package dst3.ejb.cmd;

public class CmdException extends Exception {

	String error = "unkown";

	public CmdException(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	private static final long serialVersionUID = 6667254258210597541L;

}
