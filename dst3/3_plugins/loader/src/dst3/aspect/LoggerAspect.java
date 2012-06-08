package dst3.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import dst3.dynload.IPluginExecutable;

@Aspect
public class LoggerAspect {
	@Before("execution( !@dst3.dynload.logging.Invisible * dst3.dynload.IPluginExecutable+.execute() )")
	public void atStart(JoinPoint joinPoint) {

		IPluginExecutable target = joinPoint.getTarget();

		Class<?> clazz = joinPoint.getTarget().getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (Logger.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				try {
					Logger logger = (Logger) field.get(target);
					if (logger == null)
						continue;

					logger.log(Level.INFO,
							"Execution of Plugin '" + clazz.getCanonicalName()
									+ "' started");
					return;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("Execution of Plugin '" + clazz.getCanonicalName()
				+ "' started");
	}

	@After("execution( !@dst3.dynload.logging.Invisible * dst3.dynload.IPluginExecutable+.execute() )")
	public void atEnd(JoinPoint joinPoint) {

		IPluginExecutable target = joinPoint.getTarget();

		Class<?> clazz = joinPoint.getTarget().getClass();

		for (Field field : clazz.getDeclaredFields()) {
			if (Logger.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				try {
					Logger logger = (Logger) field.get(target);
					if (logger == null)
						continue;

					logger.log(Level.INFO,
							"Execution of Plugin '" + clazz.getCanonicalName()
									+ "' finished");
					return;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("Execution of Plugin '" + clazz.getCanonicalName()
				+ "' finished");
	}
}
