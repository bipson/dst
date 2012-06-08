package dst3.scheduler;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst3.DTO.TaskDTO;

public class Scheduler implements MessageListener {

	private static QueueSession session;
	private static Queue serverQueue;
	private static Queue receiverQueue;
	private static MessageProducer producer;
	private Boolean stop = false;
	private QueueReceiver receiver;
	QueueConnection qConn;

	public static void main(String[] args) {
		new Scheduler().execute();
	}

	private void execute() {

		Scanner scan = new Scanner(System.in);

		try {
			initQueues();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		initSender();
		initReceiver();

		while (!stop) {

			System.out
					.println("Please enter command: Usage: assign <job-id> | info <task-id> | stop\n>");
			String cmd = scan.nextLine();

			if (Pattern.matches("assign [0-9]+$", cmd)) {
				parseAssign(cmd);
			} else if (Pattern.matches("info [0-9]+$", cmd)) {
				parseInfo(cmd);
			} else if (cmd.startsWith("stop")) {
				stop = true;
				break;
			} else {
				System.out
						.println("ERROR: Not a valid command! Usage: assign <job-id> | info <task-id> | stop");
			}
		}

		scan.close();

		teardown();

		System.out.println("Scheduler Service was quit by user - bye.");
	}

	private void teardown() {
		try {
			qConn.stop();
			producer.close();
			receiver.close();
			qConn.close();
			session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void initQueues() throws NamingException, JMSException {
		InitialContext jndi = new InitialContext();
		QueueConnectionFactory qFactory = (QueueConnectionFactory) jndi
				.lookup("dst.Factory");
		serverQueue = (Queue) jndi.lookup("queue.dst.ServerQueue");
		receiverQueue = (Queue) jndi.lookup("queue.dst.SchedulerQueue");
		qConn = (QueueConnection) qFactory.createConnection();
		qConn.start();
		session = qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void initSender() {
		try {
			producer = session.createProducer(serverQueue);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void initReceiver() {
		try {
			receiver = session.createReceiver(receiverQueue);
			receiver.setMessageListener(this);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	private void parseAssign(String cmd) {
		Long jobId = getSingleIntArg(cmd);
		try {
			MapMessage msg = session.createMapMessage();

			msg.setString("name", "assign");
			msg.setLong("taskid", jobId);

			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void parseInfo(String cmd) {
		Long taskId = getSingleIntArg(cmd);

		try {
			MapMessage msg = session.createMapMessage();

			msg.setString("name", "info");
			msg.setLong("taskid", taskId);

			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private Long getSingleIntArg(String cmd) {
		return Long.parseLong(cmd.split(" ")[1]);
	}

	@Override
	public void onMessage(Message message) {
		try {
			if (message != null) {
				if (message instanceof ObjectMessage) {
					ObjectMessage objectMessage = (ObjectMessage) message;
					Object object = objectMessage.getObject();

					if (object instanceof TaskDTO) {
						TaskDTO task = (TaskDTO) object;

						System.out.println(task.toString());
					} else if (object instanceof String) {
						String result = (String) object;
						System.out.println(result);
					}
				}
				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println(text);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
