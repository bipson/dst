package dst3.computer;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst3.DTO.TaskDTO;
import dst3.model.TaskStatus;

public class Computer implements MessageListener {

	private static QueueSession queueSession;
	private static TopicSession topicSession;
	private static Queue serverQueue;
	private static Topic receiverTopic;
	private static MessageProducer producer;
	private static TopicSubscriber consumer;

	private static String clusterSelector;
	private static String pcName;
	private static String complexity;
	private Boolean stop = false;

	private static HashMap<Long, TaskDTO> taskMap = new HashMap<Long, TaskDTO>();

	public static void main(String[] args) {

		if (args.length != 3) {
			System.out.println("Wrong Usage!");
			return;
		}

		pcName = args[0];

		clusterSelector = args[1];

		complexity = args[2];

		new Computer().execute();

		System.out.println("Computer Service was quit by user - bye.");
	}

	public void execute() {
		try {
			initSender();
			initReceiver();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (!stop) {

			Scanner scan = new Scanner(System.in);

			while (!stop) {
				String cmd = scan.nextLine();
				if (Pattern.matches("processed [0-9]+$", cmd)) {
					Long taskId = getSingleIntArg(cmd);
					TaskDTO task = taskMap.get(taskId);
					if (task == null) {
						System.out.println("No such task received");
						break;
					}

					task.setStatus(TaskStatus.PROCESSED);

					try {
						ObjectMessage msg = queueSession.createObjectMessage();
						msg.setStringProperty("name", "int_processed");
						msg.setObject(task);

						producer.send(msg);

						System.out.println("sending task back");
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (cmd.equals("stop")) {
					stop = true;
				} else {
					System.out
							.println("ERROR: Not a valid command! Usage: processed <task-id> | stop");
				}
			}
		}

	}

	private void initSender() throws JMSException, NamingException {
		InitialContext jndi = new InitialContext();
		serverQueue = (Queue) jndi.lookup("queue.dst.ServerQueue");
		QueueConnectionFactory qFactory = (QueueConnectionFactory) jndi
				.lookup("dst.Factory");
		QueueConnection conn = (QueueConnection) qFactory.createConnection();
		queueSession = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = queueSession.createProducer(serverQueue);
	}

	private void initReceiver() throws JMSException, NamingException {
		InitialContext jndi = new InitialContext();
		receiverTopic = (Topic) jndi.lookup("topic.dst.ComputerTopic");
		TopicConnectionFactory qFactory = (TopicConnectionFactory) jndi
				.lookup("dst.Factory");
		TopicConnection conn = (TopicConnection) qFactory.createConnection();
		conn.setClientID(pcName);

		conn.start();
		topicSession = (TopicSession) conn.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		consumer = topicSession.createDurableSubscriber(receiverTopic,
				"computer", "Comb = '" + clusterSelector + ":" + complexity
						+ "'", false);

		consumer.setMessageListener(this);
	}

	private Long getSingleIntArg(String cmd) {
		return Long.parseLong(cmd.split(" ")[1]);
	}

	@Override
	public void onMessage(Message message) {
		if (message instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) message;
				Object object = objectMessage.getObject();

				if (object instanceof TaskDTO) {
					TaskDTO task = (TaskDTO) object;

					taskMap.put(task.getId(), task);

					System.out.println("Received a task to process!");
					System.out.println(task.toString());
					System.out.println("enter 'processed " + task.getId()
							+ "' to process it");
				}
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
