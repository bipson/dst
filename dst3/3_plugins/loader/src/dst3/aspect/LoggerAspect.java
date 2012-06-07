package dst3.aspect;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggerAspect {
	@Before("execution( !@dst3.dynload.logging.Invisible * dst3.dynload.IPluginExecutable+.execute() )")
	public void atStart(JoinPoint joinPoint) {

		Class<?> clazz = joinPoint.getTarget().getClass();

		for (Field currentField : clazz.getDeclaredFields()) {
			if (Logger.class.isAssignableFrom(currentField.getType())) {
				currentField.setAccessible(true);
				try {
					Logger logger = (Logger) currentField.get(joinPoint
							.getTarget());
					if (logger == null)
						continue;

					logger.log(Level.INFO, "Aspect: Execution of "
							+ joinPoint.getTarget().getClass().toString()
							+ " started.");
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

		Class<?> clazz = joinPoint.getTarget().getClass();

		for (Field currentField : clazz.getDeclaredFields()) {
			if (Logger.class.isAssignableFrom(currentField.getType())) {
				currentField.setAccessible(true);
				try {
					Logger logger = (Logger) currentField.get(joinPoint
							.getTarget());
					if (logger == null)
						continue;

					logger.log(Level.INFO, "Aspect: Execution of "
							+ joinPoint.getTarget().getClass().toString()
							+ " started.");
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
