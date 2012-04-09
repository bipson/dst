package dst1.model;

import java.util.List;

import javax.persistence.*;

import dst1.db.interfaces.IEntity;

@Entity
@Table(name="environments")
public class Environment implements IEntity<Long> {
	
	private static final long serialVersionUID = -3609067342056221984L;
	
	private Long			id;
	private String			workflow;
	private List<String>	params;
	
	public Environment(){}

	public Environment(String workflow, List<String> params) {
		this.workflow = workflow;
		this.params = params;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="environment_id")
	public Long getId() {
		return id;
	}
	
    @Column(name="workflow")
	public String getWorkflow() {
		return workflow;
	}

    @ElementCollection
    @Column(name="param")
    @CollectionTable(
            name="environment_params",
            joinColumns=@JoinColumn(name="environment_id")
      )
    @OrderColumn(name="params_order")
    public List<String> getParams() {
		return params;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result
				+ ((workflow == null) ? 0 : workflow.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Environment))
			return false;
		Environment other = (Environment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		if (workflow == null) {
			if (other.workflow != null)
				return false;
		} else if (!workflow.equals(other.workflow))
			return false;
		return true;
	}
}
