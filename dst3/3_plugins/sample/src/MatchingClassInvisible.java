import dst3.dynload.IPluginExecutable;

public class MatchingClassInvisible implements IPluginExecutable {
	Thread executionThread;

	@Override
	public void execute() {

		executionThread = Thread.currentThread();
		try {
			System.out.println(this.getClass().getCanonicalName()
					+ ": I'm alive! I'm alive!!");
			Thread.sleep(2000);
			System.out.println(this.getClass().getCanonicalName()
					+ ": I can't feel my legs, but I'm alive!");
			Thread.sleep(2000);
			System.out.println(this.getClass().getCanonicalName()
					+ ": NOOO, a bus ran over me, bye :(");
		} catch (InterruptedException e) {
			System.out.println(this.getClass().getCanonicalName()
					+ ": execution interrupted");
		}
	}

	@Override
	public void interrupted() {
		System.out.println(this.getClass().getCanonicalName()
				+ ": I was INTERRUPTED! Oh noes!");
		executionThread.interrupt();
	}

}
