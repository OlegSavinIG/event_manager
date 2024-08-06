package ru.practicum.explorewithme.loggingaspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging repository method executions.
 */
@Aspect
@Component
public class RepositoryLoggingAspect {
    /**
     * Logger.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(RepositoryLoggingAspect.class);

    /**
     * Pointcut for repository methods.
     */
    @Pointcut("execution(* ru.practicum"
            + ".explorewithme.repository.StatisticRepository.*(..))")
    public void repositoryMethods() {
        // Pointcut for repository methods
    }

    /**
     * Logs method execution before the method is called.
     *
     * @param joinPoint the join point of the method
     */
    @Before("repositoryMethods()")
    public void logBeforeMethod(final JoinPoint joinPoint) {
        LOGGER.info("Executing method: {} with arguments: {}",
                joinPoint.getSignature(), joinPoint.getArgs());
    }

    /**
     * Logs method execution after the method has returned successfully.
     *
     * @param joinPoint the join point of the method
     * @param result    the result of the method execution
     */
    @AfterReturning(pointcut = "repositoryMethods()",
            returning = "result")
    public void logAfterMethod(final JoinPoint joinPoint,
                               final Object result) {
        LOGGER.info("Method executed successfully: {} with result: {}",
                joinPoint.getSignature(), result);
    }

    /**
     * Logs method execution after an exception is thrown.
     *
     * @param joinPoint the join point of the method
     * @param exception the exception thrown
     */
    @AfterThrowing(pointcut = "repositoryMethods()",
            throwing = "exception")
    public void logAfterThrowing(final JoinPoint joinPoint,
                                 final Throwable exception) {
        LOGGER.error("Exception in method: {} with cause: {}",
                joinPoint.getSignature(), exception.getMessage());
    }
}
