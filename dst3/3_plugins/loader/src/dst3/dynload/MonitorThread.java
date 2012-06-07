package dst3.dynload;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class MonitorThread implements Runnable {
	private File dir;
	private WatchService wService;

	public MonitorThread(File dir) {
		this.dir = dir;

		try {
			wService = FileSystems.getDefault().newWatchService();
			dir.toPath().toAbsolutePath()
					.register(wService, StandardWatchEventKinds.ENTRY_MODIFY);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		// load existing jars
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				continue;
			if (file.getPath().endsWith(".jar")) {
				try {
					new JarHandler(file).find();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			}
		}

		// monitor dir
		do {
			WatchKey wKey;

			try {
				wKey = wService.take();
			} catch (InterruptedException e) {
				return;
			}

			for (WatchEvent<?> event : wKey.pollEvents()) {
				Kind<?> kind = event.kind();
				if (kind == StandardWatchEventKinds.OVERFLOW) {
					continue;
				}

				if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {

					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path path = ev.context();

					if (path.toFile().getPath().endsWith(".jar")) {
						try {
							new JarHandler(new File(dir + "/" + path)).find();
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}

				}
			}

		} while (true);

	}
}