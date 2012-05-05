package dst2.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="executions")
public class Execution {
	
	private static final long serialVersionUID = -4905763332272953160L;

	private Long id;
	private Date start;
    private Date end;
	private JobStatus status;
	
	private Job job;
	
	private Set<Computer> computerList = new HashSet<Computer>();
	
	public Execution(){}

    public Execution(Date start, Date end, JobStatus status) {
		this.start = start;
		this.end = end;
		this.status = status;
	}
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="execution_id")
	public Long getId() {
		return id;
	}

	@Temporal(TemporalType.DATE)
    @Column(name="start_date")
	public Date getStart() {
		return start;
	}

	@Temporal(TemporalType.DATE)
    @Column(name="end_date")
	public Date getEnd() {
		return end;
	}

    @Column(name="status", columnDefinition = "ENUM('SCHEDULED','RUNNING','FAILED','FINISHED')", nullable=false)
    @Enumerated(EnumType.STRING)
	public JobStatus getStatus() {
		return status;
	}

    @OneToOne(mappedBy = "execution", optional=false, cascade=CascadeType.ALL)
	public Job getJob() {
		return job;
	}

    @ManyToMany()
    @JoinTable(
        name="execution_computer",
        joinColumns=@JoinColumn(name="execution_id"),
        inverseJoinColumns=@JoinColumn(name="computer_id")
    )
	public Set<Computer> getComputerList() {
		return computerList;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}
	
	public void setJob(Job job) {
		this.job = job;
	}

	public void setComputerList(Set<Computer> computerList) {
		this.computerList = computerList;
	}
}
