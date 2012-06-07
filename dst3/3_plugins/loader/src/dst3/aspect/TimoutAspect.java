package dst3.aspect;

import java.util.Timer;
import java.util.TimerTask;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import dst3.annotation.Timeout;
import dst3.dynload.IPluginExecutable;

@Aspect
public class TimoutAspect {

	@Around("execution(* dst3.dynload.IPluginExecutable+.execute() ) && @annotation(timeout)")
	public Object TimeOutCheck(ProceedingJoinPoint joinPoint, Timeout timeout)
			throws Throwable {
		Timer timer = new Timer();

		Long killin = timeout.timeout();

		if (!(killin == 0L)) {
			timer.schedule(new MyWatcher(joinPoint.getTarget()),
					timeout.timeout());
		}
		try {
			return joinPoint.proceed();
		} finally {
			timer.cancel();
		}

	}

	public class MyWatcher extends TimerTask {

		IPluginExecutable target;

		public MyWatcher(Object target) {
			this.target = (IPluginExecutable) target;
		}

		@Override
		public void run() {
			target.interrupted();
		}

	}
}
