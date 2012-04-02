package dst1.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="executions")
public class Execution implements Serializable {
	
	private static final long serialVersionUID = -4905763332272953160L;

	private Long		id;
	private Date		start;
	private Date		end;
	private JobStatus	status;
	
	private Job 		job;
	
	private List<Computer> computerList;
	
	public Execution(){}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="execution_id")
	public Long getId() {
		return id;
	}

    @Column(name="start_date")
	public Date getStart() {
		return start;
	}

    @Column(name="end_date")
	public Date getEnd() {
		return end;
	}

    @Column(name="status")
	public JobStatus getStatus() {
		return status;
	}

    @OneToOne(mappedBy = "job")
	public Job getJob() {
		return job;
	}

    @ManyToMany(
            targetEntity=Computer.class,
            cascade={CascadeType.PERSIST, CascadeType.MERGE}
        )
        @JoinTable(
            name="execution_computer",
            joinColumns=@JoinColumn(name="exec_id"),
            inverseJoinColumns=@JoinColumn(name="comp_id")
        )
	public List<Computer> getComputerList() {
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

	public void setComputerList(List<Computer> computerList) {
		this.computerList = computerList;
	}
}
