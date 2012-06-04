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
import dst3.ejb.util.Provider;

@MessageDriven(mappedName = "queue.dst.ServerQueue")
public class ServerBean implements MessageListener {

	HashMap<String, ICmd> cmdMap = new HashMap<String, ICmd>();

	// @Resource
	// private MessageDrivenContext mdbContext;
	@Resource(mappedName = "dst.Factory")
	private QueueConnectionFactory factory;
	@Resource(mappedName = "queue.dst.SchedulerQueue")
	private Queue schedulerQueue;
	@Resource(mappedName = "topic.dst.ClusterTopic")
	private Topic clusterTopic;

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

	@PostConstruct
	private void init() {

		Session session = null;

		QueueSender schedulerQueueSender;
		TopicPublisher clusterTopicPublisher;
		try {
			QueueConnection connection = factory.createQueueConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Provider.setEntityManager(entityManager);
			Provider.setSession(session);

			schedulerQueueSender = (QueueSender) session
					.createProducer(schedulerQueue);

			clusterTopicPublisher = (TopicPublisher) session
					.createProducer(clusterTopic);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		cmdMap.put("assign", new AssignCmd(schedulerQueueSender,
				clusterTopicPublisher));
		cmdMap.put("info", new InfoCmd(schedulerQueueSender));
		cmdMap.put("int_accept", new AcceptCmd());
	}

	@PreDestroy
	private void tearDown() {

	}
}
