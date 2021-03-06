package dst2.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import dst2.DTO.FunctionParamDTO;

@Entity
@Table(name = "function_params")
public class FunctionParam implements Serializable {
	private static final long serialVersionUID = -2730087107726506600L;

	private Long id;
	private int index_nr;
	private String className;
	private String value;

	public FunctionParam() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "function_param_id")
	public Long getId() {
		return id;
	}

	@Column(name = "index_nr")
	public int getIndex() {
		return index_nr;
	}

	@Column(name = "class_name")
	public String getClassName() {
		return className;
	}

	@Column(name = "value")
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

	public void setId(Long id) {
		this.id = id;
	}

	@Transient
	public FunctionParamDTO getDTO() {
		return new FunctionParamDTO(this.index_nr, this.className, this.value);
	}
}
