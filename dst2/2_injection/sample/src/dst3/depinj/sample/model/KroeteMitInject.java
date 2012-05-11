package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;
import dst3.depinj.annotation.Inject;

@Component()
public class KroeteMitInject {

	@ComponentId
	private Long id;

	@Inject
	private Integer si;

	public void sayHello() {
		System.out.println("Hello Kroete");
	}

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroeteMitInject kmi = new KroeteMitInject();

		controller.initialize(kmi);

		kmi.sayHello();
	}
}
