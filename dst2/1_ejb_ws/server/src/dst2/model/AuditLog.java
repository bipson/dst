package dst2.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import dst2.DTO.AuditLogDTO;
import dst2.DTO.FunctionParamDTO;

@Entity
@Table(name = "audits")
public class AuditLog {
	private Long id;
	private Date date;
	private String methodName;
	private Set<FunctionParam> params = new HashSet<FunctionParam>();
	private String result;

	public AuditLog() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_id")
	public Long getId() {
		return id;
	}

	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	@Column(name = "method_name")
	public String getMethodName() {
		return methodName;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "audit_function_params", joinColumns = @JoinColumn(name = "audit_fk"), inverseJoinColumns = @JoinColumn(name = "function_param_fk"))
	public Set<FunctionParam> getParams() {
		return params;
	}

	@Column(name = "result")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParams(Set<FunctionParam> params) {
		this.params = params;
	}

	@Transient
	public AuditLogDTO getDTO() {
		Set<FunctionParamDTO> fdtoSet = new HashSet<FunctionParamDTO>();

		for (FunctionParam param : params) {
			fdtoSet.add(param.getDTO());
		}

		AuditLogDTO dto = new AuditLogDTO(this.date, this.methodName, fdtoSet,
				this.result);

		return dto;
	}
}
