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
	public Long obtainKey() {
		return this.id;
	}
	
	
}
