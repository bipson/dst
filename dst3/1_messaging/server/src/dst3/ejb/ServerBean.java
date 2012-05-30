package dst3.ejb;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import dst3.ejb.cmd.AssignCmd;
import dst3.ejb.cmd.CmdException;
import dst3.ejb.cmd.ICmd;

@MessageDriven(mappedName = "queue.dst.MyQueue")
public class ServerBean implements MessageListener {

	HashMap<String, ICmd> cmdMap = new HashMap<String, ICmd>();

	@Resource
	private MessageDrivenContext mdbContext;

	@Override
	public void onMessage(Message message) {
		if (message instanceof MapMessage) {
			ICmd command;
			try {
				command = cmdMap.get(((MapMessage) message).getString("name"));

				if (command != null) {
					command.init((MapMessage) message);
					command.exec();
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@PostConstruct
	public void init() {
		cmdMap.put("assign", new AssignCmd());
	}
}
