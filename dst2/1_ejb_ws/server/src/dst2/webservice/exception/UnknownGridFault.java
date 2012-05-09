package dst2.webservice.exception;

public class UnknownGridFault extends Exception {

	private static final long serialVersionUID = -3029387359433125018L;

	private String code;

	public UnknownGridFault() {
		super();
		this.code = "unknown";
	}

	public UnknownGridFault(String code) {
		super();
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
