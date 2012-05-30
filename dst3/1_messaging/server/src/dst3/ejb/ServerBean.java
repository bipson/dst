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
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst3.ejb.cmd.AssignCmd;
import dst3.ejb.cmd.CmdException;
import dst3.ejb.cmd.ICmd;
import dst3.ejb.cmd.InfoCmd;
import dst3.ejb.util.Provider;

@MessageDriven(mappedName = "queue.dst.ServerQueue")
public class ServerBean implements MessageListener {

	HashMap<String, ICmd> cmdMap = new HashMap<String, ICmd>();

	@Resource
	private MessageDrivenContext mdbContext;
	@Resource(mappedName = "dst.Factory")
	private QueueConnectionFactory factory;
	@Resource(mappedName = "queue.dst.SchedulerQueue")
	private Queue schedulerQueue;

	@PersistenceContext
	private EntityManager entityManager;

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

		Session session = null;

		QueueSender schedulerQueueSender;
		try {
			QueueConnection connection = factory.createQueueConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			schedulerQueueSender = (QueueSender) session
					.createProducer(schedulerQueue);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Provider.setEntityManager(entityManager);
		Provider.setSession(session);

		cmdMap.put("assign", new AssignCmd());
		cmdMap.put("info", new InfoCmd(schedulerQueueSender));
	}
}
