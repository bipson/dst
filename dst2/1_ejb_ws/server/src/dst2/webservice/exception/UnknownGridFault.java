package dst2.webservice.exception;

public class UnknownGridFault extends Exception {

	private static final long serialVersionUID = -3029387359433125018L;

	public UnknownGridFault() {
		super("unknown");
	}

	public UnknownGridFault(String message) {
		super(message);
	}

	public UnknownGridFault(String message, Throwable cause) {
		super(message, cause);
	}
}
