package dst3.dynload;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarHandler {
	private File file;
	private URLClassLoader urlClassLoader;

	public JarHandler(File file) throws MalformedURLException {
		this.file = file;
		urlClassLoader = new URLClassLoader(new URL[] { file.toURI().toURL() });
	}

	public void find() {
		System.out.println("Will now search for correct interface in '"
				+ file.toPath() + "'");

		try {
			Enumeration<JarEntry> jar = new JarFile(file).entries();

			while (jar.hasMoreElements()) {
				JarEntry entry = jar.nextElement();
				if (entry.getName().endsWith(".class")) {

					String className = entry.getName().replace('/', '.');
					Class<?> clazz = urlClassLoader.loadClass(className
							.replace(".class", ""));

					if (IPluginExecutable.class.isAssignableFrom(clazz)) {

						System.out
								.println("Found a matching interface, will start the plugin!");

						ClassThread thread = new ClassThread(
								(IPluginExecutable) clazz.newInstance());
						new Thread(thread).start();
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}
}