package dst1.model;

import java.util.Iterator;

import javax.persistence.*;

import dst1.db.interfaces.IEntity;

@Entity
@Table(name="jobs")
public class Job implements IEntity<Long> {	

	private static final long serialVersionUID = -9182152775849509682L;

	private Long		id;	
    private boolean		isPaid;
	
    private Environment environment;
	
	private User user;
	
	private Execution execution;
	
	public Job(){}

	public Job(boolean isPaid) {
		this.isPaid = isPaid;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="job_id")
	public Long getId() {
		return id;
	}
	
    @Column(name="is_paid")
	public boolean isPaid() {
		return isPaid;
	}
    
    @OneToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name="environment_fk")
	public Environment getEnvironment() {
		return environment;
	}

    @ManyToOne(optional=false)
    @JoinColumn(name="user_fk")
	public User getUser() {
		return user;
	}

	@OneToOne(optional=false, cascade=CascadeType.ALL)
	@JoinColumn(name="execution_fk")
	public Execution getExecution() {
		return execution;
	}
	
	@Transient
	public Integer getNumCPUs() {
		
		Iterator<Computer> it = execution.getComputerList().iterator();
		Integer numCPUs = 0;
		
		while (it.hasNext())
		{
			numCPUs += it.next().getCpus(); 
		}
		
		return numCPUs;
	}

	@Transient
    public Integer getExecutionTime() {
		
		return (int) (execution.getStart().getTime() - execution.getEnd().getTime());
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setExecution(Execution execution) {
		this.execution = execution;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((environment == null) ? 0 : environment.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isPaid ? 1231 : 1237);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Job))
			return false;
		Job other = (Job) obj;
		if (environment == null) {
			if (other.environment != null)
				return false;
		} else if (!environment.equals(other.environment))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isPaid != other.isPaid)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
