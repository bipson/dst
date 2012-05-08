package dst2.model;

import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "jobs")
public class Job {

	private static final long serialVersionUID = -9182152775849509682L;

	private Long id;
	private Boolean isPaid;

	private Environment environment;

	private User user;

	private Execution execution;

	public Job() {
	}

	public Job(boolean isPaid) {
		this.isPaid = isPaid;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	public Long getId() {
		return id;
	}

	@Column(name = "is_paid")
	public Boolean isPaid() {
		return isPaid;
	}

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "environment_fk")
	public Environment getEnvironment() {
		return environment;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_fk")
	public User getUser() {
		return user;
	}

	@OneToOne
	@JoinColumn(name = "execution_fk")
	public Execution getExecution() {
		return execution;
	}

	@Transient
	public Integer getNumCPUs() {

		Iterator<Computer> it = execution.getComputerList().iterator();
		Integer numCPUs = 0;

		while (it.hasNext()) {
			numCPUs += it.next().getCpus();
		}

		return numCPUs;
	}

	@Transient
	public Integer getExecutionTime() {

		return (int) (execution.getEnd().getTime() - execution.getStart()
				.getTime());
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPaid(Boolean isPaid) {
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
