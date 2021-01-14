package com.unisoft.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.unisoft.core.util.UnisoftConfiguration.MAX_RETRY_COUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class UnisoftConfigurationTest {
    private static final String MY_CONFIGURATION = "myUnisoftConfigurationABC123";
    private static final String EXPECTED_VALUE = "aUnisoftConfigurationValueAbc123";
    private static final String UNEXPECTED_VALUE = "notMyUnisoftConfigurationValueDef456";
    private static final String DEFAULT_VALUE = "theDefaultValueGhi789";

    private static Stream<Arguments> getOrDefaultSupplier() {
        return Stream.of(
                Arguments.of(String.valueOf((byte) 42), (byte) 12, (byte) 42),
                Arguments.of(String.valueOf((short) 42), (short) 12, (short) 42),
                Arguments.of(String.valueOf(42), 12, 42),
                Arguments.of(String.valueOf(42L), 12L, 42L),
                Arguments.of(String.valueOf(42F), 12F, 42F),
                Arguments.of(String.valueOf(42D), 12D, 42D),
                Arguments.of(String.valueOf(true), false, true),
                Arguments.of("42", "12", "42")
        );
    }

    /**
     * Verifies that a runtime parameter is able to be retrieved.
     */
    @Test
    void runtimeUnisoftConfigurationFound() {
        UnisoftConfiguration configuration = spy(UnisoftConfiguration.class);
        when(configuration.loadFromProperties(MY_CONFIGURATION)).thenReturn(EXPECTED_VALUE);
        when(configuration.loadFromEnvironment(MY_CONFIGURATION)).thenReturn(null);

        assertEquals(EXPECTED_VALUE, configuration.get(MY_CONFIGURATION));
    }

    /**
     * Verifies that an environment variable is able to be retrieved.
     */
    @Test
    void environmentUnisoftConfigurationFound() {
        UnisoftConfiguration configuration = spy(UnisoftConfiguration.class);
        when(configuration.loadFromProperties(MY_CONFIGURATION)).thenReturn(null);
        when(configuration.loadFromEnvironment(MY_CONFIGURATION)).thenReturn(EXPECTED_VALUE);

        assertEquals(EXPECTED_VALUE, configuration.get(MY_CONFIGURATION));
    }

    /**
     * Verifies that null is returned when a configuration isn't found.
     */
    @Test
    void configurationNotFound() {
        UnisoftConfiguration configuration = new UnisoftConfiguration();
        assertNull(configuration.get(MY_CONFIGURATION));
    }

    /**
     * Verifies that runtime parameters are preferred over environment variables.
     */
    @Test
    void runtimeUnisoftConfigurationPreferredOverEnvironmentUnisoftConfiguration() {
        UnisoftConfiguration configuration = spy(UnisoftConfiguration.class);
        when(configuration.loadFromProperties(MY_CONFIGURATION)).thenReturn(EXPECTED_VALUE);
        when(configuration.loadFromEnvironment(MY_CONFIGURATION)).thenReturn(UNEXPECTED_VALUE);

        assertEquals(EXPECTED_VALUE, configuration.get(MY_CONFIGURATION));
    }

    /**
     * Verifies that a found configuration value is preferred over the default value.
     */
    @Test
    void foundUnisoftConfigurationPreferredOverDefault() {
        UnisoftConfiguration configuration = spy(UnisoftConfiguration.class);
        when(configuration.loadFromEnvironment(MY_CONFIGURATION)).thenReturn(EXPECTED_VALUE);

        assertEquals(EXPECTED_VALUE, configuration.get(MY_CONFIGURATION, DEFAULT_VALUE));
    }

    /**
     * Verifies that when a configuration value isn't found the default will be returned.
     */
    @Test
    void fallbackToDefaultUnisoftConfiguration() {
        UnisoftConfiguration configuration = new UnisoftConfiguration();

        assertEquals(DEFAULT_VALUE, configuration.get(MY_CONFIGURATION, DEFAULT_VALUE));
    }

    /**
     * Verifies that a found configuration value is able to be mapped.
     */
    @Test
    void foundUnisoftConfigurationIsConverted() {
        UnisoftConfiguration configuration = spy(UnisoftConfiguration.class);
        when(configuration.loadFromProperties(MY_CONFIGURATION)).thenReturn(EXPECTED_VALUE);

        assertEquals(EXPECTED_VALUE.toUpperCase(), configuration.get(MY_CONFIGURATION, String::toUpperCase));
    }

    /**
     * Verifies that when a configuration isn't found the converter returns null.
     */
    @Test
    void notFoundUnisoftConfigurationIsConvertedToNull() {
        assertNull(new UnisoftConfiguration().get(MY_CONFIGURATION, String::toUpperCase));
    }

    @Test
    void cloneUnisoftConfiguration() {
        UnisoftConfiguration configuration = new UnisoftConfiguration()
                .put("variable1", "value1")
                .put("variable2", "value2");

        UnisoftConfiguration configurationClone = configuration.clone();

        // Verify that the clone has the expected values.
        assertEquals(configuration.get("variable1"), configurationClone.get("variable1"));
        assertEquals(configuration.get("variable2"), configurationClone.get("variable2"));

        // The clone should be a separate instance, verify its modifications won't affect the original copy.
        configurationClone.remove("variable2");
        assertTrue(configuration.contains("variable2"));
    }

    @Test
    void loadValueTwice() {
        UnisoftConfiguration configuration = new UnisoftConfiguration();
        String tracingDisabled = configuration.get(MAX_RETRY_COUNT);
        String tracingDisabled2 = configuration.get(MAX_RETRY_COUNT);

        assertEquals(tracingDisabled, tracingDisabled2);
    }

    @ParameterizedTest
    @MethodSource("getOrDefaultSupplier")
    void getOrDefault(String configurationValue, Object defaultValue, Object expectedValue) {
        UnisoftConfiguration configuration = new UnisoftConfiguration()
                .put("getOrDefault", configurationValue);

        assertEquals(expectedValue, configuration.get("getOrDefault", defaultValue));
    }

    @Test
    void getOrDefaultReturnsDefault() {
        assertEquals("42", new UnisoftConfiguration().get("empty", "42"));
    }
}