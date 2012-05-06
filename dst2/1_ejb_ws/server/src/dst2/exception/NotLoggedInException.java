package dst2.exception;

public class NotLoggedInException extends Exception {

	private static final long serialVersionUID = -2469864028306765435L;

	String error;

	public NotLoggedInException() {
		super();
		error = "unknown";
	}

	public NotLoggedInException(String message) {
		super(message);
		error = message;
	}

	public String getError() {
		return error;
	}
}
