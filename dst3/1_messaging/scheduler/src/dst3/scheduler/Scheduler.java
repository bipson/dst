package dst3.scheduler;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Scheduler {

	private static QueueSession session;
	private static Queue serverQueue;
	private static Queue receiverQueue;
	private static MessageProducer producer;
	private Boolean stop = false;
	private Receiver receiver;

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

			System.out.flush();
			System.out.print("Please enter Command: \n<");
			System.out.flush();

			String cmd = scan.nextLine();

			System.out.flush();

			if (Pattern.matches("assign [0-9]+$", cmd)) {
				parseAssign(cmd);
			} else if (Pattern.matches("info [0-9]+$", cmd)) {
				parseInfo(cmd);
			} else if (cmd.startsWith("stop")) {
				stop = true;
				receiver.stop = true;
				break;
			} else {
				System.out
						.println("ERROR: Not a valid command! Usage: assign <job-id> | info <task-id> | stop");
			}
		}
		if (producer != null) {
			try {
				producer.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Scheduler Service was quit by user - bye.");
	}

	private void initQueues() throws NamingException, JMSException {
		InitialContext jndi = new InitialContext();
		QueueConnectionFactory qFactory = (QueueConnectionFactory) jndi
				.lookup("dst.Factory");
		serverQueue = (Queue) jndi.lookup("queue.dst.ServerQueue");
		receiverQueue = (Queue) jndi.lookup("queue.dst.SchedulerQueue");
		QueueConnection qConn = (QueueConnection) qFactory.createConnection();
		session = qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void initSender() {
		try {
			producer = session.createProducer(serverQueue);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initReceiver() {
		receiver = new Receiver(receiverQueue, session);

		new Thread(receiver).start();
	}

	private void parseAssign(String cmd) {
		Integer jobId = getSingleIntArg(cmd);
		MapMessage msg;
		try {
			msg = session.createMapMessage();

			msg.setString("name", "assign");
			msg.setLong("taskid", jobId);

			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void parseInfo(String cmd) {
		Integer taskId = getSingleIntArg(cmd);

		MapMessage msg;
		try {
			msg = session.createMapMessage();

			msg.setString("name", "info");
			msg.setLong("taskid", taskId);

			producer.send(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private Integer getSingleIntArg(String cmd) {
		return Integer.parseInt(cmd.split(" ")[1]);
	}

	public static QueueSession getSession() {
		return Scheduler.session;
	}

	private class Receiver implements Runnable {

		private Queue queue;
		private QueueSession session;
		public boolean stop = false;

		public Receiver(Queue queue, QueueSession session) {
			this.queue = queue;
			this.session = session;
		}

		@Override
		public void run() {
			try {
				QueueReceiver queueReceiver = session.createReceiver(queue);

				System.out.println("Receiver started...");

				while (!stop) {

					System.out.println("Receiver in loop");

					Message message = queueReceiver.receive();
					if (message != null) {
						System.out.println("Message not null");
						if (message instanceof ObjectMessage) {
							ObjectMessage objectMessage = (ObjectMessage) message;
							System.out.println("Received Task: "
									+ (String) (objectMessage.getObject()));
						}
					}
				}

				if (queueReceiver != null) {
					queueReceiver.close();
				}

				System.out.println("receiver going down...");

			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
	}
}
