package dst3.dynload;

public class ClassThread implements Runnable {
	private IPluginExecutable exec;

	public ClassThread(IPluginExecutable exec) {
		this.exec = exec;
	}

	@Override
	public void run() {
		exec.execute();
	}

}
