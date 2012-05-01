package dst2.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dst2.ejb.TestingBeanRemote;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Context ctx = null;
		try {
			ctx = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		// name is whatever JNDI name you gave it 
		TestingBeanRemote testbean = null;
		try {
			testbean = (TestingBeanRemote) ctx.lookup("dst2.ejb.TestingBeanRemote");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testbean.InsertTestEntities();
	}

}
