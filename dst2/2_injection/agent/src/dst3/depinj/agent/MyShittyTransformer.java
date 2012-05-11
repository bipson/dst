package dst3.depinj.agent;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.NotFoundException;
import dst3.depinj.annotation.Component;

public class MyShittyTransformer implements ClassFileTransformer {

	private static final String injection = "dst3.depinj.IInjectionController ic = dst3.depinj.InjectionController.getController();\n"
			+ "ic.initialize(this);";

	private static final String[] ignore = { "$" };

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		for (String skip : ignore) {
			if (className.startsWith(skip)) {
				return null;
			}
		}

		className = className.replace("/", ".");
		try {
			CtClass clazz = ClassPool.getDefault().getCtClass(className);
			if (clazz.hasAnnotation(Component.class)) {

				try {
					CtConstructor constructors[] = clazz.getConstructors();
					for (CtConstructor constructor : constructors) {
						constructor.insertBeforeBody(injection);
					}
					return clazz.toBytecode();
				} catch (CannotCompileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

}
