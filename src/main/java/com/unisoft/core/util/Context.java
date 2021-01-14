package com.unisoft.core.util;

import com.unisoft.core.annotations.Immutable;
import com.unisoft.core.util.log.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * {@code Context} offers a means of passing arbitrary data (key-value pairs) to any data context.
 * Most applications do not need to pass arbitrary data to the pipeline and can pass {@code Context.NONE} or
 * {@code null}.
 * <p>
 * Each context object is immutable. The {@link #addData(Object, Object)} method creates a new
 * {@code Context} object that refers to its parent, forming a linked list.
 *
 * @author omar.H.Ajmi
 * @since 18/10/2020
 */
@Immutable
public class Context {
    /**
     * Signifies that no data needs to be passed to any data context.
     */
    public static final Context NONE = new Context(null, null, null);
    private final Context parent;
    private final Object key;
    private final Object value;
    Logger log = LoggerFactory.getLogger(Context.class);

    /**
     * Constructs a new {@link Context} object.
     *
     * <p><strong>Code samples</strong></p>
     * <p>
     * {@codesnippet com.unisoft.core.util.context#object-object}
     *
     * @param key   The key with which the specified value should be associated.
     * @param value The value to be associated with the specified key.
     * @throws IllegalArgumentException If {@code key} is {@code null}.
     */
    public Context(Object key, Object value) {
        this.parent = null;
        this.key = Objects.requireNonNull(key, "'key' cannot be null.");
        this.value = value;
    }

    private Context(Context parent, Object key, Object value) {
        this.parent = parent;
        this.key = key;
        this.value = value;
    }

    /**
     * Creates a new immutable {@link Context} object with all the keys and values provided by
     * the input {@link Map}.
     *
     * <p><strong>Code samples</strong></p>
     * <p>
     * {@codesnippet com.unisoft.core.util.context.of#map}
     *
     * @param keyValues The input key value pairs that will be added to this context.
     * @return Context object containing all the key-value pairs in the input map.
     * @throws IllegalArgumentException If {@code keyValues} is {@code null} or empty
     */
    public static Context of(Map<Object, Object> keyValues) {
        if (CoreUtil.isNullOrEmpty(keyValues)) {
            throw new IllegalArgumentException("Key value map cannot be null or empty");
        }

        Context context = null;
        for (Map.Entry<Object, Object> entry : keyValues.entrySet()) {
            if (context == null) {
                context = new Context(entry.getKey(), entry.getValue());
            } else {
                context = context.addData(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    /**
     * Adds a new immutable {@link Context} object with the specified key-value pair to
     * the existing {@link Context} chain.
     *
     * <p><strong>Code samples</strong></p>
     * <p>
     * {@codesnippet com.unisoft.core.util.context.addData#object-object}
     *
     * @param key   The key with which the specified value should be associated.
     * @param value The value to be associated with the specified key.
     * @return the new {@link Context} object containing the specified pair added to the set of pairs.
     * @throws IllegalArgumentException If {@code key} is {@code null}.
     */
    public Context addData(Object key, Object value) {
        if (key == null) {
            LogUtil.logExceptionAsError(log, () -> {
                throw new IllegalArgumentException("key cannot be null");
            });
        }
        return new Context(this, key, value);
    }

    /**
     * Scans the linked-list of {@link Context} objects looking for one with the specified key.
     * Note that the first key found, i.e. the most recently added, will be returned.
     *
     * <p><strong>Code samples</strong></p>
     * <p>
     * {@codesnippet com.unisoft.core.util.context.getData#object}
     *
     * @param key The key to search for.
     * @return The value of the specified key if it exists.
     * @throws IllegalArgumentException If {@code key} is {@code null}.
     */
    public Optional<Object> getData(Object key) {
        if (key == null) {
            LogUtil.logExceptionAsError(log, () -> {
                throw new IllegalArgumentException("key cannot be null");
            });
        }
        for (Context c = this; c != null; c = c.parent) {
            if (c.key.equals(key)) {
                return Optional.of(c.value);
            }
        }
        return Optional.empty();
    }

    /**
     * same as {@link Context#getData(Object key)}
     * only difference is that it tries to cast the found value to a target type
     *
     * @note <p>
     * <font color="red"> should be used with care, and only if you are sure the casting operation wont fail.</font>
     * </p>
     */
    public <T> Optional<T> getData(Object key, Class<T> type) {
        return this.getData(key).map(type::cast);
    }

    /**
     * Scans the linked-list of {@link Context} objects populating a {@link Map} with the values of the context.
     *
     * <p><strong>Code samples</strong></p>
     * <p>
     * {@codesnippet com.unisoft.core.util.Context.getValues}
     *
     * @return A map containing all values of the context linked-list.
     */
    public Map<Object, Object> getValues() {
        return getValuesHelper(new HashMap<>());
    }

    private Map<Object, Object> getValuesHelper(Map<Object, Object> values) {
        if (key != null) {
            values.putIfAbsent(key, value);
        }

        return (parent == null) ? values : parent.getValuesHelper(values);
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder()
                .append("context{")
                .append("key=").append(this.key.toString())
                .append(", value=").append(this.value.toString());
        if (Objects.isNull(this.parent)) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append(", parent=").append(this.parent.toString()).append("}");
        }
        return stringBuilder.toString();
    }
}
