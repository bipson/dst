package dst3.aspect;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import dst3.dynload.IPluginExecutable;

@Aspect
public class LoggerAspect {
	@Before("execution( !@dst3.dynload.logging.Invisible * dst3.dynload.IPluginExecutable+.execute() )")
	public void atStart(JoinPoint joinPoint) {

		IPluginExecutable target = (IPluginExecutable) joinPoint.getTarget();

		Class<?> clazz = joinPoint.getTarget().getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (Logger.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				field.setAccessible(true);
				Logger logger = null;
				try {
					logger = (Logger) field.get(target);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if (logger == null)
					continue;

				logger.log(Level.INFO,
						"Execution of Plugin '" + clazz.getCanonicalName()
								+ "' started");
				return;
			}
		}

		System.out.println("Execution of Plugin '" + clazz.getCanonicalName()
				+ "' started");
	}

	@After("execution( !@dst3.dynload.logging.Invisible * dst3.dynload.IPluginExecutable+.execute() )")
	public void atEnd(JoinPoint joinPoint) {

		IPluginExecutable target = (IPluginExecutable) joinPoint.getTarget();

		Class<?> clazz = joinPoint.getTarget().getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (Logger.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				Logger logger = null;
				try {
					logger = (Logger) field.get(target);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				if (logger == null)
					continue;

				logger.log(Level.INFO,
						"Execution of Plugin '" + clazz.getCanonicalName()
								+ "' finished");
				return;
			}
		}

		System.out.println("Execution of Plugin '" + clazz.getCanonicalName()
				+ "' finished");
	}
}
