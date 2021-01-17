package com.unisoft.core.util.log;

import org.slf4j.Logger;

/**
 * @author omar.H.Ajmi
 * @since 18/10/2020
 */
public class LogUtil {
    private LogUtil() {
    }

    public static void info(Logger logger, String message, Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(message, args);
        }
    }

    public static void error(Logger logger, String message, Object... args) {
        if (logger.isErrorEnabled()) {
            logger.error(message, args);
        }
    }

    public static void debug(Logger logger, String message, Object... args) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, args);
        }
    }

    public static void warn(Logger logger, String message, Object... args) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, args);
        }
    }

    public static void logExceptionAsError(Logger logger, Callable executable) {
        try {
            executable.call();
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    public static void logExceptionAsWarn(Logger logger, Callable executable) {
        try {
            executable.call();
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    public static RuntimeException logExceptionAsError(Logger logger, RuntimeException throwable) {
        logger.error(throwable.getMessage(), throwable);
        return throwable;
    }

    public static RuntimeException logExceptionAsWarn(Logger logger, RuntimeException throwable) {
        logger.warn(throwable.getMessage(), throwable);
        return throwable;
    }
}
