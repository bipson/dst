package dst2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "function_params")
public class FunctionParam {
	private Long id;
	private Integer index;
	private String className;
	private String value;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "audit_param_id")
	public Long getId() {
		return id;
	}

	@Column(name = "index")
	public Integer getIndex() {
		return index;
	}

	@Column(name = "class_name")
	public String getClassName() {
		return className;
	}

	@Column(name = "value")
	public String getValue() {
		return value;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
