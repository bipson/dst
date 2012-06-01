package dst3.cluster;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst3.DTO.TaskDTO;
import dst3.model.TaskComplexity;
import dst3.model.TaskStatus;

public class Cluster {

	private static Session session;
	private static Queue serverQueue;
	private static Queue receiverQueue;
	private static MessageProducer producer;
	private static Topic topic;
	private static Boolean stop = false;
	private static TopicSubscriber subscriber;

	private static String clusterName;

	public static void main(String[] args) {
		new Cluster().execute(args);
	}

	private void execute(String[] args) {

		if (args.length != 1) {
			System.out.println("Wrong Usage");
		}

		clusterName = args[0];

		try {
			initQueues();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		try {
			initSender();
			initReceiver();
		} catch (JMSException e1) {
			e1.printStackTrace();
			teardown();
			return;
		}

		while (!stop) {

			Message message;
			try {
				message = subscriber.receive();

				if (message instanceof ObjectMessage) {
					ObjectMessage objMessage = (ObjectMessage) message;

					try {
						Object object = objMessage.getObject();
						if (object instanceof TaskDTO) {
							TaskDTO taskDTO = (TaskDTO) object;
							taskDTO = getUserInput(taskDTO);

							if (taskDTO == null) {
								return;
							}

							try {
								ObjectMessage msg = session
										.createObjectMessage();

								msg.setStringProperty("name", "int_accept");
								msg.setObject(taskDTO);

								producer.send(msg);
							} catch (JMSException e) {
								e.printStackTrace();
							}
						}
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		teardown();

		System.out.println("Scheduler Service was quit by user - bye.");
	}

	private void teardown() {
		// TODO close everything
	}

	private void initQueues() throws NamingException, JMSException {
		InitialContext jndi = new InitialContext();
		serverQueue = (Queue) jndi.lookup("queue.dst.ServerQueue");
		topic = (Topic) jndi.lookup("queue.dst.ClusterTopic");

		QueueConnectionFactory qFactory = (QueueConnectionFactory) jndi
				.lookup("dst.Factory");
		Connection Conn = (Connection) qFactory.createConnection();
		Conn.start();
		session = Conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void initSender() throws JMSException {
		producer = session.createProducer(serverQueue);
	}

	private void initReceiver() throws JMSException {
		subscriber = session.createDurableSubscriber(topic, clusterName);
	}

	public static Session getSession() {
		return Cluster.session;
	}

	public TaskDTO getUserInput(TaskDTO taskDTO) {

		Scanner scan = new Scanner(System.in);

		System.out.println("Received a Task, what should i do?");
		System.out.println("Usage: accept {EASY|CHALLENGING} | deny | stop");
		String cmd = scan.nextLine();

		if (Pattern.matches("accept (EASY|CHALLENGING)$", cmd)) {
			String complexityStr = cmd.split(" ")[1];
			if (complexityStr.equals("EASY")) {
				taskDTO.setComplexity(TaskComplexity.EASY);
			} else {
				taskDTO.setComplexity(TaskComplexity.CHALLENGING);
			}
			taskDTO.setStatus(TaskStatus.READY_FOR_PROCESSING);
			taskDTO.setRatedBy(clusterName);
		} else if (Pattern.matches("deny$", cmd)) {
			taskDTO.setStatus(TaskStatus.PROCESSING_NOT_POSSIBLE);
			taskDTO.setRatedBy(clusterName);
		} else if (cmd.startsWith("stop")) {
			return null;
		} else {
			System.out
					.println("ERROR: Not a valid command! Usage: accept {EASY|CHALLENGING} | deny | stop");
		}

		return taskDTO;
	}

}
