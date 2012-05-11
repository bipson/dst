package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;

@Component()
public class KroeteMitId {
	private String foo;

	@ComponentId
	private Long id;

	private Integer si;

	private void sayHello() {
		System.out.println("KroetemitID Hello");
	}

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		KroeteMitId kmi = new KroeteMitId();

		controller.initialize(kmi);

		kmi.sayHello();
	}
}
