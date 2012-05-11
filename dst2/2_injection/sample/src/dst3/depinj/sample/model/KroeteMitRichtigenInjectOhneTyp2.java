package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;
import dst3.depinj.annotation.Inject;

@Component()
public class KroeteMitRichtigenInjectOhneTyp2 {

	@ComponentId
	private Long id;

	@Inject(required = true)
	private String si;

	public void sayHello() {
		System.out.println("KroeteMitRichtigenId Hello");
	}

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroeteMitRichtigenInjectOhneTyp2 kmi = new KroeteMitRichtigenInjectOhneTyp2();

		controller.initialize(kmi);

		kmi.sayHello();
	}
}
