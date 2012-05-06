package dst2.exception;

public class NotEnoughCPUsAvailableException extends Exception {

	private static final long serialVersionUID = -8296745257800402469L;

	String error;

	public NotEnoughCPUsAvailableException() {
		super();
		error = "unknown";
	}

	public NotEnoughCPUsAvailableException(String message) {
		super(message);
		error = message;
	}

	public String getError() {
		return error;
	}
}
