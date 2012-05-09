package dst2.DTO;

import java.io.Serializable;
import java.util.Date;

public class ExecutionDTO implements Serializable {

	private static final long serialVersionUID = 6983499434709581305L;

	private Date start;
	private Date end;
	private Integer numCPUs;

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public Integer getNumCPUs() {
		return numCPUs;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setNumCPUs(Integer numCPUs) {
		this.numCPUs = numCPUs;
	}

	@Override
	public String toString() {
		return "Execution:\n" + "[start = " + start + "\n" + "end = " + end
				+ "\n" + "numCPUs = " + numCPUs + "\n";
	}
}
