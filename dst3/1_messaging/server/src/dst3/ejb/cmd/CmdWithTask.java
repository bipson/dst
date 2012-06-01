package dst3.ejb.cmd;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import dst3.DTO.TaskDTO;
import dst3.model.Task;

public abstract class CmdWithTask implements ICmd {
	protected Task task;

	@Override
	public void init(Message message) throws CmdException {
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage objMessage = (ObjectMessage) message;
				TaskDTO taskDTO = (TaskDTO) objMessage.getObject();
				task = new Task();
				task.setId(taskDTO.getId());
				task.setJobId(taskDTO.getJobId());
				task.setComplexity(taskDTO.getComplexity());
				task.setStatus(taskDTO.getStatus());
				task.setRatedBy(taskDTO.getRatedBy());
			} catch (JMSException e) {
				throw new CmdException(e.getErrorCode());
			}
		} else {
			throw new CmdException("Wrong Message type supplied!");
		}
	}
}
