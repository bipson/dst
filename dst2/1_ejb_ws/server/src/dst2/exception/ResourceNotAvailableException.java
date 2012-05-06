package dst2.exception;

public class ResourceNotAvailableException extends Exception {

	private static final long serialVersionUID = 7580860109634024555L;
	String error;

	public ResourceNotAvailableException() {
		super();
		error = "unknown";
	}

	public ResourceNotAvailableException(String message) {
		super(message);
		error = message;
	}

	public String getError() {
		return error;
	}
}
