package dst3.cluster;

import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jms.JMSException;
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

import dst3.DTO.TaskDTO;
import dst3.model.TaskComplexity;
import dst3.model.TaskStatus;

public class Cluster {

	private static InitialContext jndi;
	private static QueueConnectionFactory qFactory;
	private static Queue serverQueue;
	private static Queue receiverQueue;
	private static MessageProducer producer;
	private static QueueReceiver queueReceiver;
	private static QueueSession queueSession;
	private static QueueConnection qConn;

	private static Boolean stop = false;
	Thread userInputThread;

	private static String clusterName;
	private static TaskDTO taskDTO;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Wrong Usage");
			return;
		}

		clusterName = args[0];

		Cluster cluster = new Cluster();

		try {
			cluster.initQueues();
			cluster.initSender();
			cluster.initReceiver();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}

		cluster.exec();

		System.out.println("Cluster Service was quit by user - bye.");
	}

	private void exec() {

		userInputThread = new Thread(new UserInputChecker(this));
		userInputThread.start();

		while (!stop) {
			try {

				Message message = queueReceiver.receive(1000);

				if (message instanceof ObjectMessage) {
					ObjectMessage objMessage = (ObjectMessage) message;

					Object object = objMessage.getObject();
					if (object instanceof TaskDTO) {

						qConn.close();

						taskDTO = (TaskDTO) object;

						System.out
								.println("Received a Task, what should i do?");
						System.out
								.println("Usage: accept {EASY|CHALLENGING} | deny | stop");
						try {
							synchronized (this) {
								this.wait();
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (stop)
							break;

						try {
							initQueues();
							initSender();
							initReceiver();
						} catch (NamingException e) {
							e.printStackTrace();
						}

						ObjectMessage msg = queueSession.createObjectMessage();

						msg.setStringProperty("name", "int_accept");
						msg.setObject(taskDTO);

						System.out.println("sending task back");

						taskDTO = null;

						producer.send(msg);
					}
				}
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		}

		teardown();

		return;
	}

	private static void teardown() {
		try {
			qConn.stop();
			producer.close();
			queueReceiver.close();
			qConn.close();
			queueSession.close();
			jndi.close();

		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private void initQueues() throws NamingException, JMSException {
		jndi = new InitialContext();
		qFactory = (QueueConnectionFactory) jndi.lookup("dst.Factory");
		serverQueue = (Queue) jndi.lookup("queue.dst.ServerQueue");
		receiverQueue = (Queue) jndi.lookup("queue.dst.ClusterQueue");
		qConn = (QueueConnection) qFactory.createConnection();
		qConn.start();
		queueSession = qConn
				.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void initSender() {
		try {
			producer = queueSession.createProducer(serverQueue);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void initReceiver() throws JMSException {
		queueReceiver = queueSession.createReceiver(receiverQueue);
	}

	public class UserInputChecker implements Runnable {
		Cluster cluster;

		public UserInputChecker(Cluster cluster) {
			this.cluster = cluster;
		}

		public void run() {

			Scanner scan = new Scanner(System.in);

			while (!stop) {
				String cmd = scan.nextLine();
				if (Pattern.matches("accept (EASY|CHALLENGING)$", cmd)) {
					if (taskDTO == null) {
						System.out.println("No task received yet! wait for it");
						break;
					}
					String complexityStr = cmd.split(" ")[1];
					if (complexityStr.equals("EASY")) {
						taskDTO.setComplexity(TaskComplexity.EASY);
					} else {
						taskDTO.setComplexity(TaskComplexity.CHALLENGING);
					}
					taskDTO.setStatus(TaskStatus.READY_FOR_PROCESSING);
					taskDTO.setRatedBy(clusterName);
					synchronized (cluster) {
						cluster.notify();
					}
				} else if (Pattern.matches("deny$", cmd)) {
					if (taskDTO == null) {
						System.out.println("No task received yet! wait for it");
						break;
					}
					taskDTO.setStatus(TaskStatus.PROCESSING_NOT_POSSIBLE);
					taskDTO.setRatedBy(clusterName);
					synchronized (cluster) {
						cluster.notify();
					}
				} else if (cmd.equals("stop")) {
					stop = true;
					taskDTO = null;
					synchronized (cluster) {
						cluster.notify();
					}
				} else {
					System.out
							.println("ERROR: Not a valid command! Usage: accept {EASY|CHALLENGING} | deny | stop");
				}
			}
		}
	}
}
