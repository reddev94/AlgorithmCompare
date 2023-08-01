package com.reddev.algorithmcompare.core.aop;

import com.reddev.algorithmcompare.common.domain.exception.DatabaseException;
import com.reddev.algorithmcompare.common.util.AlgorithmCompareUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@EnableAspectJAutoProxy
@Component
@Log4j2
@RequiredArgsConstructor
public class LoggingAspect {

	private static final int LIMIT_LOG_SIZE = 1000;

	private final HttpServletRequest request;

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void classAnnotatedWithRestController() {}

	@Pointcut("execution(public * *(..))")
	public void publicMethod() {}

	@Pointcut("publicMethod() && classAnnotatedWithRestController()")
	public void publicMethodInsideAClassMarkedWithAtRestController() {}

	@Pointcut("execution(public * org.springframework.data.repository.Repository+.*(..))")
	public void monitorQueryDB() {}

	@Around("publicMethodInsideAClassMarkedWithAtRestController()")
	public Object logMillisAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String apiPath = request.getRequestURI();
		final StopWatch stopWatch = new StopWatch();
		log.info("Call to rest api " + apiPath + " with request: " + getArguments(joinPoint));
		Object result = executeMillisMethod(joinPoint, stopWatch);
		log.info("Rest api " + apiPath + " executed in {} ms, with response: " + getResult(joinPoint, result),
				stopWatch.getTotalTimeMillis());
		return result;
	}

	@Around("monitorQueryDB()")
	public Object queryExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		String[] classNameUnparsed = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
		String className = classNameUnparsed[classNameUnparsed.length - 1];
		String methodName = joinPoint.getSignature().getName();
		final StopWatch stopWatch = new StopWatch();
		Object result;
		try {
			log.debug("Execution of query " + className + "." + methodName + "() with param: " + getArguments(joinPoint));
			result = executeMillisMethod(joinPoint, stopWatch);
		} catch (Exception e) {
			log.error("Error during query execution: ", e);
			throw new DatabaseException(AlgorithmCompareUtil.RESULT_CODE_DB_ERROR, AlgorithmCompareUtil.RESULT_DESCRIPTION_DB_ERROR);
		}
		log.info("Query " + className + "." + methodName + "() executed in " + stopWatch.getTotalTimeMillis() + " ms");
		log.debug("with response: " + getResult(joinPoint, result));
		return result;
	}

	private Object executeMillisMethod(ProceedingJoinPoint joinPoint, StopWatch stopWatch) throws Throwable {
		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();
		return result;
	}

	private String getArguments(ProceedingJoinPoint joinPoint) {
		int argsLength = 0;
		if (joinPoint != null && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
			argsLength = Arrays.toString(joinPoint.getArgs()).length();
		}
		String arguments = "empty";
		if (argsLength > 0) {
			arguments = Arrays.toString(joinPoint.getArgs()).substring(0,
					Math.min(argsLength, LIMIT_LOG_SIZE))
					+ (argsLength > LIMIT_LOG_SIZE ? "...request too big, truncated." : "");
		}
		return arguments;
	}

	private String getResult(ProceedingJoinPoint joinPoint, Object result) {
		String resultString = "empty";
		if (result != null) {
			int resultLength = result.toString().length();
			resultString = result.toString().substring(0,
					Math.min(resultLength, LIMIT_LOG_SIZE))
					+ (resultLength > LIMIT_LOG_SIZE ? "...response too big, truncated." : "");
		}
		return resultString;
	}

}