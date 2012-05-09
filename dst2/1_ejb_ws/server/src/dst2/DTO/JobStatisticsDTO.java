package dst2.DTO;

import java.io.Serializable;
import java.util.Set;

public class JobStatisticsDTO implements Serializable {
	private static final long serialVersionUID = -2089051969417063887L;

	private String gridname;
	private Set<ExecutionDTO> execDTO;

	public String getGridname() {
		return gridname;
	}

	public Set<ExecutionDTO> getExecDTO() {
		return execDTO;
	}

	public void setGridname(String gridname) {
		this.gridname = gridname;
	}

	public void setExecDTO(Set<ExecutionDTO> execDTO) {
		this.execDTO = execDTO;
	}
}
