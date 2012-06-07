public class NotAMatchingClass {

	public void execute() {
		try {
			System.out.println("I'm alive! I'm alive!!");
			Thread.sleep(2000);
			System.out.println("I can't feel my legs, but I'm alive!");
			Thread.sleep(2000);
			System.out.println("NOOO, a bus ran over me, bye :(");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void interrupted() {
		System.out.println("I was interrupted! Oh noes!");
	}

}
