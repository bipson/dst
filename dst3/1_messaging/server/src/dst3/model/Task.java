package dst3.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Task {

	public enum TaskComplexity {
		UNRATED, EASY, CHALLENGING
	}

	public enum TaskStatus {
		ASSIGNED, READY_FOR_PROCESSING, PROCESSING_NOT_POSSIBLE, PROCESSED
	}

	private Long id;
	private Long jobId;
	private TaskStatus status;
	private String ratedBy;
	private TaskComplexity complexity;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	@Column
	public Long getJobId() {
		return jobId;
	}

	@Column
	public TaskStatus getStatus() {
		return status;
	}

	@Column
	public String getRatedBy() {
		return ratedBy;
	}

	@Column(columnDefinition = "ENUM('UNRATED', 'EASY', 'CHALLENGING')")
	@Enumerated(EnumType.STRING)
	public TaskComplexity getComplexity() {
		return complexity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(columnDefinition = "ENUM('ASSIGNED', 'READY_FOR_PROCESSING', 'PROCESSING_NOT_POSSIBLE', 'PROCESSED')")
	@Enumerated(EnumType.STRING)
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	public void setComplexity(TaskComplexity complexity) {
		this.complexity = complexity;
	}

}
