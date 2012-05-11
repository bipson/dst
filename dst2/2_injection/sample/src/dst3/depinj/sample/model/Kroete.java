package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Inject;

public class Kroete {
	private String foo;

	@Inject
	private Integer si;

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroeteMitInject kmi = new KroeteMitInject();

		controller.initialize(kmi);

		kmi.sayHello();
	}
}
