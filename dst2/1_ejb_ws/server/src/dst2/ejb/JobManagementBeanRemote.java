package dst2.ejb;

import java.util.List;

import javax.ejb.Remote;

import dst2.exception.NotEnoughCPUsAvailableException;
import dst2.exception.NotLoggedInException;
import dst2.exception.ResourceNotAvailableException;
import dst2.model.Job;

@Remote
public interface JobManagementBeanRemote {

	public void loginUser(String username, String password);

	public void addJob(Long grid_id, Integer numCPUs, String workflow,
			List<String> params) throws NotEnoughCPUsAvailableException;

	public void checkout() throws NotLoggedInException,
			ResourceNotAvailableException;

	public List<Job> getJobList(Long grid_id);

	public void clearJobList(Long grid_id);
}
