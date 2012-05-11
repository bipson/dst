package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Component;

@Component
public class KroetenKind {
	private String foo;

	private Integer si;

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroeteMitInject kmi = new KroeteMitInject();

		controller.initialize(kmi);

		kmi.sayHello();
	}
}
