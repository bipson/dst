package dst3.scheduler;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Scheduler {

	protected Session session;
	protected Queue serverQueue;
	protected Queue receiverQueue;

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

		initReceiver();

		while (true) {

			System.out.println("Please enter Command: ");
			System.out.print("< ");

			String cmd = scan.nextLine();

			if (Pattern.matches("assign [0-9]+$", cmd)) {
				parseAssign(cmd);
			} else if (Pattern.matches("info [0-9]+$", cmd)) {
				parseInfo(cmd);
			} else if (cmd.startsWith("stop")) {
				break;
			} else {
				System.out
						.println("ERROR: Not a valid command! Usage: assign <job-id> | info <task-id> | quit");
			}
		}

	}

	private void initQueues() throws NamingException, JMSException {
		InitialContext jndi = new InitialContext();
		QueueConnectionFactory qFactory = (QueueConnectionFactory) jndi
				.lookup("dst.Factory");
		serverQueue = (Queue) jndi.lookup("queue.dst.ServerQueue");
		receiverQueue = (Queue) jndi.lookup("queue.dst.SchedulerQueue");
		QueueConnection qConn = (QueueConnection) qFactory.createConnection();
		session = qConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void initReceiver() {
		Receiver receiver = new Receiver(receiverQueue);

		new Thread(receiver).start();
	}

	private void parseAssign(String cmd) {
		Integer jobId = getSingleIntArg(cmd);
		// Set the JMS message
		MapMessage msg;
		try {
			msg = session.createMapMessage();

			msg.setString("name", "assign");
			msg.setLong("taskid", jobId);

			// Send JMS message
			session.createProducer(serverQueue).send(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
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

			// Send JMS message
			session.createProducer(serverQueue).send(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Integer getSingleIntArg(String cmd) {
		return Integer.parseInt(cmd.split(" ")[1]);
	}

	private class Receiver implements Runnable {

		private Queue queue;

		public Receiver(Queue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
	}
}
