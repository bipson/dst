package dst2.ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst2.DTO.AuditLogDTO;
import dst2.ejb.generated.ExecutionDTO;
import dst2.ejb.generated.JobStatistics;
import dst2.ejb.generated.JobStatisticsDTO;
import dst2.ejb.generated.Service;
import dst2.ejb.generated.UnknownGridFault_Exception;
import dst2.ejb.interfaces.GeneralManagementBeanRemote;
import dst2.ejb.interfaces.JobManagementBeanRemote;
import dst2.ejb.interfaces.TestBeanRemote;
import dst2.exception.NotEnoughCPUsAvailableException;
import dst2.exception.NotLoggedInException;
import dst2.exception.ResourceNotAvailableException;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Context ctx = null;
		Scanner scan = new Scanner(System.in);

		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TestBeanRemote testBean = null;
		GeneralManagementBeanRemote manageBean = null;
		JobManagementBeanRemote jobBean = null;

		try {
			testBean = (TestBeanRemote) ctx
					.lookup("java:global/dst2_1/TestBean");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out
				.println("Will now insert Test-Entities, press Enter to continue...");
		scan.nextLine();

		testBean.InsertTestEntities();

		System.out
				.println("Inserted Test-Entities, press Enter to continue...");
		scan.nextLine();

		try {
			manageBean = (GeneralManagementBeanRemote) ctx
					.lookup("java:global/dst2_1/GeneralManagementBean");
		} catch (NamingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		manageBean.setPrice(100, new BigDecimal(30));
		manageBean.setPrice(1000, new BigDecimal(15));
		manageBean.setPrice(5000, new BigDecimal(10));

		System.out.println("Inserted PriceSteps, press Enter to continue...");
		scan.nextLine();

		// Afterwards obtain a reference to your JobAssignmentBean, try to
		// login
		// with invalid values, then login successfully, add some valid job
		// assignments, request the current assigned amount of jobs and
		// finally
		// successfully submit your temporary job list.

		try {
			jobBean = (JobManagementBeanRemote) ctx
					.lookup("java:global/dst2_1/JobManagementBean");
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		jobBean.loginUser("gacksi", "notapassword");

		jobBean.loginUser("gacksi", "foo1");

		try {
			String[] params = { "ad", "daf", "dfad" };
			jobBean.addJob(1L, 1, "asdfa",
					new ArrayList<String>(Arrays.asList(params)));
		} catch (NotEnoughCPUsAvailableException e) {
			System.out.println("OHOH, not enough CPUs :( Something is wrong : "
					+ e.getError());
		}
		try {
			String[] params2 = { "aadfd", "ddfaaf", "dfadfad" };
			jobBean.addJob(2L, 1, "dfaga",
					new ArrayList<String>(Arrays.asList(params2)));
		} catch (NotEnoughCPUsAvailableException e) {
			System.out.println("OHOH, not enough CPUs :( Something is wrong : "
					+ e.getError());
		}

		try {
			jobBean.checkout();
		} catch (NotLoggedInException e) {
			System.out.println("User not logged in! " + e.getError());
		} catch (ResourceNotAvailableException e) {
			System.out.println("Upsie :" + e.getError());
		}

		System.out
				.println("User assigned jobs, hopefully succeeded, press Enter to continue...");
		scan.nextLine();

		// � Replay the last step with a different user, but this time try to
		// assign more jobs for a grid than there are free computers. Delete the
		// job assignments for a grid, request the assigned amount of jobs and
		// finally successfully submit your temporary job list.

		try {
			jobBean = (JobManagementBeanRemote) ctx
					.lookup("java:global/dst2_1/JobManagementBean");
		} catch (NamingException e1) {
			e1.printStackTrace();
		}

		jobBean.loginUser("quacksi", "foo2");

		try {
			String[] params = { "adad", "dafaff", "dfadfad" };
			jobBean.addJob(1L, 1, "asadfadfa",
					new ArrayList<String>(Arrays.asList(params)));
		} catch (NotEnoughCPUsAvailableException e) {
			System.out
					.println("OHOH, not enough CPUs :(  - Something is wrong : "
							+ e.getError());
		}
		try {
			String[] params = { "adad", "dafaff", "dfadfad" };
			jobBean.addJob(1L, 10, "asadfadfa",
					new ArrayList<String>(Arrays.asList(params)));
		} catch (NotEnoughCPUsAvailableException e) {
			System.out
					.println("This is the exception we were waiting for, one job failed - great : "
							+ e.getError());
		}
		try {
			String[] params2 = { "aadfadfd", "ddffdaaaf", "dfadsdffad" };
			jobBean.addJob(2L, 1, "dfafadfga",
					new ArrayList<String>(Arrays.asList(params2)));
		} catch (NotEnoughCPUsAvailableException e) {
			System.out
					.println("OHOH, not enough CPUs :( - Something is wrong : "
							+ e.getError());
		}

		System.out
				.println("User tried to add some jobs, one should have failed");

		System.out.println("This are the number of listed jobs for Grid 1: "
				+ jobBean.getJobList(1L));
		System.out.println("This are the number of listed jobs for Grid 2: "
				+ jobBean.getJobList(2L));

		jobBean.clearJobList(1L);
		System.out.println("cleared Joblist for grid: " + 1L);
		System.out.println("let's see the count again");

		System.out.println("This are the number of listed jobs for Grid 1: "
				+ jobBean.getJobList(1L));
		System.out.println("This are the number of listed jobs for Grid 2: "
				+ jobBean.getJobList(2L));

		try {
			jobBean.checkout();
		} catch (NotLoggedInException e1) {
			System.out.println("Not logged in : " + e1.getError());
		} catch (ResourceNotAvailableException e1) {
			System.out.println("OHOH : " + e1.getError());
		}

		// � Wait for some time so that your jobs are finished.
		System.out
				.println("Will now sleep for 1 Minute to finish jobs. Stretch your feet");
		try {
			Thread.sleep(60000);
			System.out
					.println("You may now check the database, press Enter to continue...");
			scan.nextLine();
		} catch (InterruptedException e) {
			System.out.println("Who dares to interrupt a princess's sleep?!");
		}

		System.out.println("Will now generate your bill...");

		// � Use the GeneralManagementBean to get the bill for all finished
		// jobs.
		Future<String> result = manageBean.getBill("gacksi");

		System.out.println("Waiting for result, please be patient...");

		try {
			System.out.println(result.get());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// � Finally get all saved audits from the Audit-Interceptor.
		System.out
				.println("Almost done, for the audit-log please press Enter...");
		scan.nextLine();

		for (AuditLogDTO log : manageBean.getAuditLog())
			System.out.println(log);

		System.out.println("End of Part 1");

		System.out.println("Press Enter to start Part 2:");
		scan.nextLine();

		JobStatistics jobStat = new JobStatistics();
		JobStatisticsDTO jobDTO = null;

		Service port = jobStat.getJobPort();

		try {
			jobDTO = port.getStatistics("grid1");
		} catch (UnknownGridFault_Exception e) {
			System.out.println("Gridname rejected by Service : "
					+ e.getMessage());
		}

		if (jobDTO != null) {
			System.out.println("Grid Name: " + jobDTO.getGridname());
			System.out.println("------------------------------");
			for (ExecutionDTO exec : jobDTO.getExecDTO()) {
				System.out.println("   Execution:");
				System.out.println("   start: " + exec.getStart());
				System.out.println("   end: " + exec.getEnd());
				System.out.println("   Number of CPUs: " + exec.getNumCPUs());
				System.out.println("   - - - - - - - - - - - - - - ");
			}
		}

		System.out.println("End of Part 2");
	}
}
