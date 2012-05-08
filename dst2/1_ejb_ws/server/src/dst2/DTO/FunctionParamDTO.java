package dst2.DTO;

public class FunctionParamDTO {
	private int index_nr;
	private String className;
	private String value;

	public FunctionParamDTO() {
		super();
	}

	public FunctionParamDTO(int index_nr, String className, String value) {
		super();
		this.index_nr = index_nr;
		this.className = className;
		this.value = value;
	}

	public int getIndex() {
		return index_nr;
	}

	public String getClassName() {
		return className;
	}

	public String getValue() {
		return value;
	}

	public void setIndex(int index) {
		this.index_nr = index;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FunctionParamDTO [index_nr=" + index_nr + ", className="
				+ className + ", value=" + value + "]";
	}

}
