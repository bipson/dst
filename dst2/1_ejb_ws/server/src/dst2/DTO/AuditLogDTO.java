package dst2.DTO;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AuditLogDTO implements Serializable, Comparable<AuditLogDTO> {

	private static final long serialVersionUID = 840031001158435089L;

	private Date date;
	private String methodName;
	private Set<FunctionParamDTO> params = new HashSet<FunctionParamDTO>();
	private String result;

	public AuditLogDTO() {
		super();
	}

	public AuditLogDTO(Date date, String methodName,
			Set<FunctionParamDTO> params, String result) {
		super();
		this.date = date;
		this.methodName = methodName;
		this.params = params;
		this.result = result;
	}

	public Date getDate() {
		return date;
	}

	public String getMethodName() {
		return methodName;
	}

	public Set<FunctionParamDTO> getParams() {
		return params;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParams(Set<FunctionParamDTO> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return ("AuditLogDTO [date=" + date + ", methodName=" + methodName
				+ ", params=" + params + ", result=" + result + "]");
	}

	public int compareTo(AuditLogDTO o) {
		return (int) (this.date.getTime() - o.date.getTime());

	}
}
