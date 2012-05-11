package dst3.depinj.sample.model;

import dst3.depinj.InjectionController;
import dst3.depinj.ScopeType;
import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;

@Component(scope = ScopeType.SINGLETON)
public class BesondereKroete {

	@ComponentId
	private Long id;

	private Integer si;

	public void sayHello() {
		System.out.println("Hello Kroete");
	}

	public static void runB() {
		InjectionController controller = InjectionController.getController();

		BesondereKroete bk = new BesondereKroete();
		BesondereKroete bk2 = new BesondereKroete();

		controller.initialize(bk);
		controller.initialize(bk2);

		bk.sayHello();
	}

	public static void runA() {
		InjectionController controller = InjectionController.getController();

		BesondereKroete bk = new BesondereKroete();

		controller.initialize(bk);

		bk.sayHello();
	}
}
