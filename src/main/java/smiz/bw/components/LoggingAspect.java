package smiz.bw.components;

import jdk.nashorn.internal.ir.RuntimeNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * Spring AOP aspect class implementing logging of controller methods' calls.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && "
			+ "@annotation(org.springframework.web.bind.annotation.RequestMapping) && "
			+ "execution(* *(..))"
	)
	protected void controller() {
	};

	@Before("controller()")
	public void logBefore(JoinPoint joinPoint) {
		log.debug(String.format("Entering in Method :  %s.%s",
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName()));
		log.debug(String.format("\targuments :  %s", Arrays.toString(joinPoint.getArgs())));
	}

	@AfterReturning(pointcut = "controller()", returning = "retVal")
	public void logAfter(JoinPoint joinPoint, Object retVal) {
		log.debug(String.format("Returning from Method :  %s.%s",
				joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName()));
		log.debug(String.format("\treturn value :  %s", retVal));
	}
}
