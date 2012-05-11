package dst3.depinj.sample;

import dst3.depinj.sample.model.BesondereKroete;
import dst3.depinj.sample.model.KroeteMitFalschemInject;
import dst3.depinj.sample.model.KroeteMitId;
import dst3.depinj.sample.model.KroeteMitInject;
import dst3.depinj.sample.model.KroeteMitInjectKind;
import dst3.depinj.sample.model.KroeteMitRichtigenInjectOhneTyp;
import dst3.depinj.sample.model.KroeteOhneId;
import dst3.depinj.sample.model.KroetenKind;

public class Run {

	public static void main(String[] args) {

		try {
			KroeteOhneId.runA();
		} catch (RuntimeException e) {
			System.out
					.println("Initializing a Kroete without ID failed, great : "
							+ e.getMessage());
		}

		KroeteMitId.runA();

		KroeteMitInject.runA();

		try {
			KroeteMitInjectKind.runA();
		} catch (RuntimeException e) {
			System.out
					.println("Initializing a Kroete without ID set already by parent failed, great : "
							+ e.getMessage());
		}

		KroetenKind.runA();

		BesondereKroete.runA();

		try {
			BesondereKroete.runB();
		} catch (RuntimeException e) {
			System.out.println("Reinitializing of a Singleton failed, great : "
					+ e.getMessage());
		}

		try {
			KroeteMitFalschemInject.runA();
		} catch (RuntimeException e) {
			System.out.println("Trying to inject a wrong type failed, great : "
					+ e.getMessage());
		}

		KroeteMitRichtigenInjectOhneTyp.runA();
	}
}
