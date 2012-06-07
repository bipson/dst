package dst3.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggerAspect {
	@Before("execution( (!@dst3.aspect.annotation.Invisible *) dst3.dynload.IPluginExecutable.execute() )")
	// @Before("execution( * dst3.dynload.IPluginExecutable.execute())")
	public void atStart(JoinPoint joinPoint) {
		System.out.println("Execution of Plugin '"
				+ joinPoint.getTarget().getClass().getCanonicalName()
				+ "' started");
	}

	@After("execution( (!@dst3.aspect.annotation.Invisible *) dst3.dynload.IPluginExecutable.execute())")
	public void atEnd(JoinPoint joinPoint) {
		System.out.println("Execution of Plugin '"
				+ joinPoint.getTarget().getClass().getCanonicalName()
				+ "' finished");
	}
}
