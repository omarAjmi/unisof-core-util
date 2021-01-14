package com.unisoft.core.util.log;

/**
 * callable contract used to be able to log exceptions upon invocation
 *
 * @author omar.H.Ajmi
 * @since 18/10/2020
 */
@FunctionalInterface
public interface Callable {

    void call();
}
