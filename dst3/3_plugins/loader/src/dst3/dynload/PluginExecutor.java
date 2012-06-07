package dst3.dynload;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

public class PluginExecutor implements IPluginExecutor {

	private HashMap<File, Thread> dirMonitors = new HashMap<File, Thread>();

	@Override
	public void monitor(File dir) {
		if (dirMonitors.containsKey(dir))
			return;

		if (!dir.isDirectory())
			return;

		dirMonitors.put(dir, new Thread(new MonitorThread(dir)));

		System.out.println("put '" + dir.toPath()
				+ "' on the list of monitored dirs");
	}

	@Override
	public void stopMonitoring(File dir) {
		if (dirMonitors.containsKey(dir)) {
			dirMonitors.remove(dir);
			System.out.println("removed '" + dir.toPath()
					+ "' from the list of monitored dirs");
		}
	}

	@Override
	public void start() {
		for (Thread monitor : dirMonitors.values()) {
			if (monitor.isAlive())
				continue;

			monitor.start();
		}

	}

	@Override
	public void stop() {
		for (Entry<File, Thread> monitoredDirs : dirMonitors.entrySet()) {

			if (monitoredDirs.getValue().isAlive())
				monitoredDirs.getValue().interrupt();

			stopMonitoring(monitoredDirs.getKey());

		}

	}

}
