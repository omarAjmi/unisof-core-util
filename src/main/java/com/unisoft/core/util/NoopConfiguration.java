package com.unisoft.core.util;

import java.util.function.Function;

/**
 * Noop Configuration used to opt out of using global configurations when constructing client libraries.
 *
 * @author omar.H.Ajmi
 * @since 21/10/2020
 */
public class NoopConfiguration extends UnisoftConfiguration {
    @Override
    public String get(String name) {
        return null;
    }

    @Override
    public <T> T get(String name, T defaultValue) {
        return defaultValue;
    }

    @Override
    public <T> T get(String name, Function<String, T> converter) {
        return null;
    }

    @Override
    public NoopConfiguration put(String name, String value) {
        return this;
    }

    @Override
    public String remove(String name) {
        return null;
    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public NoopConfiguration clone() {
        return new NoopConfiguration();
    }
}
