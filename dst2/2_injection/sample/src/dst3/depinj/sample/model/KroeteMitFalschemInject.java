package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;
import dst3.depinj.annotation.Inject;

@Component()
public class KroeteMitFalschemInject {

	@ComponentId
	private Long id;

	@Inject(specificType = String.class, required = true)
	private Integer si;

	public void sayHello() {
		System.out.println("Hello Kroete");
	}

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroeteMitFalschemInject kmi = new KroeteMitFalschemInject();

		controller.initialize(kmi);

		kmi.sayHello();
	}
}
