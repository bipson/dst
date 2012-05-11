package dst3.depinj.agent;

import java.lang.instrument.Instrumentation;

public class PreMain {
	public static void premain(String args, Instrumentation instrumentation) {
		instrumentation.addTransformer(new MyShittyTransformer());
	}
}
