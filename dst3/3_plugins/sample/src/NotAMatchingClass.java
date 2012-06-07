public class NotAMatchingClass {

	Thread executionThread;

	public void execute() {

		executionThread = Thread.currentThread();

		try {
			System.out.println(this.getClass().getCanonicalName()
					+ ": I'm alive! I'm alive!!");
			Thread.sleep(2000);
			System.out.println(this.getClass().getCanonicalName()
					+ ": I can't feel my legs, but I'm alive!");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			System.out.println(this.getClass().getCanonicalName()
					+ ": execution interrupted");
		}
	}

	public void interrupted() {
		System.out.println(this.getClass().getCanonicalName()
				+ ": I was INTERRUPTED! Oh noes!");
		executionThread.interrupt();
	}

}