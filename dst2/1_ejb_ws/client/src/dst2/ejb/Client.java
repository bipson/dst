package dst2.ejb;

import java.math.BigDecimal;
import java.util.Scanner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

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

		TestingBeanRemote testBean = null;
		GeneralManagementBeanRemote manageBean = null;
		JobManagementBeanRemote jobBean = null;

		try {
			testBean = (TestingBeanRemote) ctx
					.lookup("dst2.ejb.TestingBeanRemote");
			manageBean = (GeneralManagementBeanRemote) ctx
					.lookup("dst2.ejb.GeneralManagementBeanRemote");
			jobBean = (JobManagementBeanRemote) ctx
					.lookup("dst2.ejb.JobManagementBeanRemote");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		testBean.InsertTestEntities();

		while (scan.next() != "\n")
			;

		manageBean.setPrice(100, new BigDecimal(30));
		manageBean.setPrice(1000, new BigDecimal(15));
		manageBean.setPrice(5000, new BigDecimal(10));

		// Afterwards obtain a reference to your JobAssignmentBean, try to login
		// with invalid values, then login successfully, add some valid job
		// assignments, request the current assigned amount of jobs and finally
		// successfully submit your temporary job list.
		// ¥ Replay the last step with a different user, but this time try to
		// assign more jobs for a grid than there are free computers. Delete the
		// job assignments for a grid, request the assigned amount of jobs and
		// finally successfully submit your temporary job list.
		// ¥ Wait for some time so that your jobs are finished.
		// ¥ Use the GeneralManagementBean to get the bill for all finished
		// jobs.
		// ¥ Finally get all saved audits from the Audit-Interceptor.

	}

}
