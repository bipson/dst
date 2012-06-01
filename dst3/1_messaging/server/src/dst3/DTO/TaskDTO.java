package dst3.DTO;

import java.io.Serializable;

import dst3.model.TaskComplexity;
import dst3.model.TaskStatus;

public class TaskDTO implements Serializable {

	private static final long serialVersionUID = -5855208452712230201L;

	private Long id;
	private Long jobId;
	private TaskStatus status;
	private String ratedBy;
	private TaskComplexity complexity;

	public TaskDTO(Long id, Long jobId, TaskStatus status, String ratedBy,
			TaskComplexity complexity) {
		super();
		this.id = id;
		this.jobId = jobId;
		this.status = status;
		this.ratedBy = ratedBy;
		this.complexity = complexity;
	}

	public Long getId() {
		return id;
	}

	public Long getJobId() {
		return jobId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public String getRatedBy() {
		return ratedBy;
	}

	public TaskComplexity getComplexity() {
		return complexity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public void setRatedBy(String ratedBy) {
		this.ratedBy = ratedBy;
	}

	public void setComplexity(TaskComplexity complexity) {
		this.complexity = complexity;
	}

	@Override
	public String toString() {
		return "TaskDTO [id=" + id + ", jobId=" + jobId + ", status=" + status
				+ ", ratedBy=" + ratedBy + ", complexity=" + complexity + "]";
	}

}
