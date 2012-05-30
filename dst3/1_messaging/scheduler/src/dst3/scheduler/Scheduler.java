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

	private static Session session;
	private static Queue queue;

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		try {
			initQueue();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}

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

	private static void initQueue() throws NamingException, JMSException {
		// Get context
		InitialContext jndi = new InitialContext();

		// Get queue connection factory
		QueueConnectionFactory qFactory = (QueueConnectionFactory) jndi
				.lookup("dst.Factory");

		// Get queue
		queue = (Queue) jndi.lookup("queue.dst.MyQueue");

		// Get queue connection
		QueueConnection qConn = (QueueConnection) qFactory.createConnection();

		// Get session
		session = qConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private static void parseAssign(String cmd) {
		Integer jobId = getSingleIntArg(cmd);
		// Set the JMS message
		MapMessage msg;
		try {
			msg = session.createMapMessage();

			msg.setString("name", "assign");
			msg.setLong("taskid", jobId);

			// Send JMS message
			session.createProducer(queue).send(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void parseInfo(String cmd) {
		Integer taskId = getSingleIntArg(cmd);
		System.out.println(taskId);
	}

	private static Integer getSingleIntArg(String cmd) {
		return Integer.parseInt(cmd.split(" ")[1]);
	}
}
