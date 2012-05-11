package dst3.depinj.sample;

import dst3.depinj.annotation.Component;
import dst3.depinj.annotation.ComponentId;
import dst3.depinj.annotation.Inject;
import dst3.depinj.sample.model.KroeteMitId;
import dst3.depinj.sample.model.KroetenKind;

@Component
public class RunWithAgent {

	@ComponentId
	Long id;

	@Inject
	Integer foo;

	@Inject(required = true, specificType = String.class)
	String lamarama;

	@Inject(required = false)
	private KroeteMitId kroeteMitID;

	@Inject(required = true)
	private KroetenKind kroetenKind;

	private void si() {
		System.out.println("I am a component, and i have injected attributes");
	}

	public static void main(String[] args) {

		RunWithAgent rwa = new RunWithAgent();

		rwa.si();
	}
}
