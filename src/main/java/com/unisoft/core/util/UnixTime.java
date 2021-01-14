package com.unisoft.core.util;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author omar.H.Ajmi
 * @since 19/10/2020
 */
public final class UnixTime {
    /**
     * The actual datetime object.
     */
    private final OffsetDateTime dateTime;

    /**
     * Creates aUnixTime object with the specified DateTime.
     *
     * @param dateTime The DateTime object to wrap
     */
    public UnixTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Creates a UnixTime object with the specified DateTime.
     *
     * @param unixSeconds The Unix seconds value
     */
    public UnixTime(long unixSeconds) {
        this.dateTime = OffsetDateTime.ofInstant(Instant.ofEpochSecond(unixSeconds), ZoneOffset.UTC);
    }

    /**
     * Get the underlying DateTime.
     *
     * @return The underlying DateTime
     */
    public OffsetDateTime getDateTime() {
        return this.dateTime;
    }

    @Override
    public String toString() {
        return String.valueOf(dateTime.toEpochSecond());
    }

    @Override
    public int hashCode() {
        return this.dateTime.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof UnixTime)) {
            return false;
        }

        UnixTime rhs = (UnixTime) obj;
        return this.dateTime.equals(rhs.getDateTime());
    }
}
