import dst3.dynload.IPluginExecutable;

public class MatchingClass implements IPluginExecutable {

	@Override
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

	@Override
	public void interrupted() {
		System.out.println("I was interrupted! Oh noes!");
	}

}
