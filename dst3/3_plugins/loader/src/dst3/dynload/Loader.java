package dst3.dynload;

import java.io.File;
import java.util.Scanner;

public class Loader {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		PluginExecutor pExecutor = new PluginExecutor();

		pExecutor.monitor(new File("plugins"));
		System.out.println("PluginExecutor running...");

		pExecutor.start();
		scanner.nextLine();
		pExecutor.stop();
	}
}
