package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Component;

@Component
public class KroetenKind extends KroeteMitId {
	private String foo;

	private Integer si;

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroetenKind kmi = new KroetenKind();

		controller.initialize(kmi);

		kmi.sayHello();
	}

	private void sayHello() {
		System.out.println("Kroetenkind hallo");
	}
}
