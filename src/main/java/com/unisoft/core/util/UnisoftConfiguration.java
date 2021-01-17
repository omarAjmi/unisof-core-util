package com.unisoft.core.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author omar.H.Ajmi
 * @since 21/10/2020
 */
public class UnisoftConfiguration implements Cloneable {
    /**
     * No-op {@link UnisoftConfiguration} object used to opt out of using global configurations when constructing client
     * libraries.
     */
    public static final UnisoftConfiguration NONE = new NoopConfiguration();
    protected static final String MAX_RETRY_COUNT = "MAX_RETRY_COUNT";
    private static final UnisoftConfiguration GLOBAL_CONFIGURATION = new UnisoftConfiguration();
    protected final String[] DEFAULT_CONFIGURATIONS = {
            MAX_RETRY_COUNT
    };
    private final ConcurrentMap<String, String> configurations;

    /**
     * Constructs a configuration containing the known Unisoft properties constants.
     */
    public UnisoftConfiguration() {
        this.configurations = new ConcurrentHashMap<>();
        loadBaseConfiguration(this);
    }

    private UnisoftConfiguration(ConcurrentMap<String, String> configurations) {
        this.configurations = new ConcurrentHashMap<>(configurations);
    }

    /**
     * Gets the global configuration store shared by all client libraries.
     *
     * @return The global configuration store.
     */
    public static UnisoftConfiguration getGlobalConfiguration() {
        return GLOBAL_CONFIGURATION;
    }

    /*
     * Attempts to convert the configuration value to {@code T}.
     *
     * If the value is null or empty then the default value is returned.
     *
     * @param value Configuration value retrieved from the map.
     * @param defaultValue Default value to return if the configuration value is null or empty.
     * @param <T> Generic type that the value is converted to if not null or empty.
     * @return The converted configuration, if null or empty the default value.
     */
    @SuppressWarnings("unchecked")
    private static <T> T convertOrDefault(String value, T defaultValue) {
        // Value is null or empty, return the default.
        if (CoreUtil.isNullOrEmpty(value)) {
            return defaultValue;
        }

        // Check the default value's type to determine how it needs to be converted.
        Object convertedValue;
        if (defaultValue instanceof Byte) {
            convertedValue = Byte.parseByte(value);
        } else if (defaultValue instanceof Short) {
            convertedValue = Short.parseShort(value);
        } else if (defaultValue instanceof Integer) {
            convertedValue = Integer.parseInt(value);
        } else if (defaultValue instanceof Long) {
            convertedValue = Long.parseLong(value);
        } else if (defaultValue instanceof Float) {
            convertedValue = Float.parseFloat(value);
        } else if (defaultValue instanceof Double) {
            convertedValue = Double.parseDouble(value);
        } else if (defaultValue instanceof Boolean) {
            convertedValue = Boolean.parseBoolean(value);
        } else {
            convertedValue = value;
        }

        return (T) convertedValue;
    }

    /**
     * Gets the value of the configuration.
     *
     * @param name Name of the configuration.
     * @return Value of the configuration if found, otherwise {@code null}.
     */
    public String get(String name) {
        return getOrLoad(name);
    }

    /**
     * Gets the value of the configuration converted to {@code T}.
     * <p>
     * If no configuration is found, the {@code defaultValue} is returned.
     *
     * @param name         Name of the configuration.
     * @param defaultValue Value to return if the configuration isn't found.
     * @param <T>          Type that the configuration is converted to if found.
     * @return The converted configuration if found, otherwise the default value is returned.
     */
    public <T> T get(String name, T defaultValue) {
        return convertOrDefault(getOrLoad(name), defaultValue);
    }

    /**
     * Gets the converted value of the configuration.
     *
     * @param name      Name of the configuration.
     * @param converter Converter used to map the configuration to {@code T}.
     * @param <T>       Generic type that the configuration is converted to if found.
     * @return The converted configuration if found, otherwise null.
     */
    public <T> T get(String name, Function<String, T> converter) {
        String value = getOrLoad(name);
        if (CoreUtil.isNullOrEmpty(value)) {
            return null;
        }

        return converter.apply(value);
    }

    /*
     * Attempts to get the value of the configuration from the configuration store, if the value isn't found then it
     * attempts to load it from the runtime parameters then the environment variables.
     *
     * If no configuration is found null is returned.
     *
     * @param name Name of the configuration.
     * @return The configuration value from either the configuration store, runtime parameters, or environment
     * variable, in that order, if found, otherwise null.
     */
    private String getOrLoad(String name) {
        String value = configurations.get(name);
        if (value != null) {
            return value;
        }

        value = load(name);
        if (value != null) {
            configurations.put(name, value);
            return value;
        }

        return null;
    }

    /*
     * Attempts to load the configuration from the environment.
     *
     * The runtime parameters are checked first followed by the environment variables.
     *
     * @param name Name of the configuration.
     * @return If found the loaded configuration, otherwise null.
     */
    private String load(String name) {
        String value = loadFromProperties(name);

        if (value != null) {
            return value;
        }

        return loadFromEnvironment(name);
    }

    String loadFromEnvironment(String name) {
        return System.getenv(name);
    }

    String loadFromProperties(String name) {
        return System.getProperty(name);
    }

    /**
     * Adds a configuration with the given value.
     * <p>
     * If a configuration with the same name already exists, this will update it to the passed value.
     *
     * @param name  Name of the configuration.
     * @param value Value of the configuration.
     * @return The updated Configuration object.
     */
    public UnisoftConfiguration put(String name, String value) {
        configurations.put(name, value);
        return this;
    }

    /**
     * Removes the configuration.
     *
     * @param name Name of the configuration.
     * @return If the configuration was removed the value of it, otherwise {@code null}.
     */
    public String remove(String name) {
        return configurations.remove(name);
    }

    /**
     * Determines if the configuration exists.
     *
     * @param name Name of the configuration.
     * @return True if the configuration exists, otherwise false.
     */
    public boolean contains(String name) {
        return configurations.containsKey(name);
    }

    /**
     * @return A clone of the Configuration object.
     */
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public UnisoftConfiguration clone() {
        return new UnisoftConfiguration(configurations);
    }

    private void loadBaseConfiguration(UnisoftConfiguration configuration) {
        for (String config : DEFAULT_CONFIGURATIONS) {
            String value = load(config);
            if (value != null) {
                configuration.put(config, value);
            }
        }
    }
}
