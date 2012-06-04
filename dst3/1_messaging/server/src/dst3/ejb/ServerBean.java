package dst3.ejb;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicPublisher;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst3.ejb.cmd.AcceptCmd;
import dst3.ejb.cmd.AssignCmd;
import dst3.ejb.cmd.CmdException;
import dst3.ejb.cmd.ICmd;
import dst3.ejb.cmd.InfoCmd;
import dst3.ejb.cmd.ProcessCmd;
import dst3.ejb.util.Provider;

@MessageDriven(mappedName = "queue.dst.ServerQueue")
public class ServerBean implements MessageListener {

	HashMap<String, ICmd> cmdMap = new HashMap<String, ICmd>();

	@Resource(mappedName = "dst.Factory")
	private QueueConnectionFactory factory;
	@Resource(mappedName = "queue.dst.SchedulerQueue")
	private Queue schedulerQueue;
	@Resource(mappedName = "queue.dst.ClusterQueue")
	private Queue clusterQueue;
	@Resource(mappedName = "topic.dst.ComputerTopic")
	private Topic computerTopic;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void onMessage(Message message) {
		if (message instanceof MapMessage) {
			ICmd command;
			try {
				command = cmdMap.get(((MapMessage) message).getString("name"));

				if (command != null) {
					command.init((Message) message);
					command.exec();
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmdException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (message instanceof ObjectMessage) {
			ICmd command;
			try {
				command = cmdMap.get(((ObjectMessage) message)
						.getStringProperty("name"));

				if (command != null) {
					command.init((Message) message);
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

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {

		Session session = null;

		QueueSender schedulerQueueSender;
		QueueSender clusterQueueSender;
		TopicPublisher computerPublisher;
		try {
			QueueConnection connection = factory.createQueueConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Provider.setEntityManager(entityManager);
			Provider.setSession(session);

			schedulerQueueSender = (QueueSender) session
					.createProducer(schedulerQueue);

			clusterQueueSender = (QueueSender) session
					.createProducer(clusterQueue);

			computerPublisher = (TopicPublisher) session
					.createProducer(computerTopic);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		cmdMap.put("assign", new AssignCmd(schedulerQueueSender,
				clusterQueueSender));
		cmdMap.put("info", new InfoCmd(schedulerQueueSender));
		cmdMap.put("int_accept", new AcceptCmd(computerPublisher));
		cmdMap.put("int_processed", new ProcessCmd(schedulerQueueSender));
	}

	@PreDestroy
	private void tearDown() {

	}
}
