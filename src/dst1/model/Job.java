package dst1.model;

import java.io.Serializable;
import java.util.Iterator;

import javax.persistence.*;

@Entity
@Table(name="jobs")
public class Job implements Serializable {	

	private static final long serialVersionUID = -9182152775849509682L;

	private Long		id;
	private boolean		isPaid;
	
	private Environment environment;
	
	private User user;
	
	private Execution execution;
	
	public Job(){}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="job_id")
	public Long getId() {
		return id;
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
    
    
    @Column(name="is_paid")
	public boolean isPaid() {
		return isPaid;
	}

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="environment_fk", nullable=false)
    public Environment getEnvironment() {
		return environment;
	}

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_fk", nullable=false)
	public User getUser() {
		return user;
	}

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="execution_fk")
	public Execution getExecution() {
		return execution;
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

}
