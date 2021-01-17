package com.unisoft.core.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author omar.H.Ajmi
 * @since 18/10/2020
 */
public class FluxUtil {

    private FluxUtil() {
        // no-op
    }

    public static Mono<byte[]> collectBytesInByteBufferStream(Flux<ByteBuffer> stream) {
        return stream
                .collect(ByteArrayOutputStream::new, FluxUtil::accept)
                .map(ByteArrayOutputStream::toByteArray);
    }

    private static void accept(ByteArrayOutputStream byteOutputStream, ByteBuffer byteBuffer) {
        try {
            byteOutputStream.write(byteBufferToArray(byteBuffer));
        } catch (IOException e) {
            throw new RuntimeException("Error occurred writing ByteBuffer to ByteArrayOutputStream.", e);
        }
    }

    /**
     * Gets the content of the provided ByteBuffer as a byte array. This method will create a new byte array even if the
     * ByteBuffer can have optionally backing array.
     *
     * @param byteBuffer the byte buffer
     * @return the byte array
     */
    public static byte[] byteBufferToArray(ByteBuffer byteBuffer) {
        int length = byteBuffer.remaining();
        byte[] byteArray = new byte[length];
        byteBuffer.get(byteArray);
        return byteArray;
    }

    /**
     * Checks if a type is Flux&lt;ByteBuffer&gt;.
     *
     * @param entityType the type to check
     * @return whether the type represents a Flux that emits ByteBuffer
     */
    public static boolean isFluxByteBuffer(Type entityType) {
        if (TypeUtil.isTypeOrSubTypeOf(entityType, Flux.class)) {
            final Type innerType = TypeUtil.getTypeArguments(entityType)[0];
            return TypeUtil.isTypeOrSubTypeOf(innerType, ByteBuffer.class);
        }
        return false;
    }

    /**
     * This method converts the incoming {@code subscriberContext} from {@link reactor.util.context.Context Reactor
     * Context} to {@link Context Unisoft Context} and calls the given lambda function with this context and returns a
     * single entity of type {@code T}
     * <p>
     * If the reactor context is empty, {@link Context#NONE} will be used to call the lambda function
     * </p>
     *
     * <p><strong>Code samples</strong></p>
     *
     * @param serviceCall The lambda function that makes the service call into which unisoft context will be passed
     * @param <T>         The type of response returned from the service call
     * @return The response from service call
     */
    public static <T> Mono<T> withContext(Function<Context, Mono<T>> serviceCall) {
        return withContext(serviceCall, Collections.emptyMap());
    }

    /**
     * This method converts the incoming {@code subscriberContext} from {@link reactor.util.context.Context Reactor
     * Context} to {@link Context Unisoft Context}, adds the specified context attributes and calls the given lambda
     * function with this context and returns a single entity of type {@code T}
     * <p>
     * If the reactor context is empty, {@link Context#NONE} will be used to call the lambda function
     * </p>
     *
     * @param serviceCall       serviceCall The lambda function that makes the service call into which unisoft context will be
     *                          passed
     * @param contextAttributes The map of attributes sent by the calling method to be set on {@link Context}.
     * @param <T>               The type of response returned from the service call
     * @return The response from service call
     */
    public static <T> Mono<T> withContext(Function<Context, Mono<T>> serviceCall,
                                          Map<String, String> contextAttributes) {
        return Mono.subscriberContext()
                .map(context -> {
                    final Context[] unisoftContext = new Context[]{Context.NONE};

                    if (!CoreUtil.isNullOrEmpty(contextAttributes)) {
                        contextAttributes.forEach((key, value) -> unisoftContext[0] = unisoftContext[0].addData(key, value));
                    }

                    if (!context.isEmpty()) {
                        context.stream().forEach(entry ->
                                unisoftContext[0] = unisoftContext[0].addData(entry.getKey(), entry.getValue()));
                    }

                    return unisoftContext[0];
                })
                .flatMap(serviceCall);
    }

    /**
     * Converts an Unisoft context to Reactor context. If the Unisoft context is {@code null} or empty, {@link
     * reactor.util.context.Context#empty()} will be returned.
     *
     * @param context The Unisoft context.
     * @return The Reactor context.
     */
    public static reactor.util.context.Context toReactorContext(Context context) {
        if (context == null) {
            return reactor.util.context.Context.empty();
        }

        // Filter out null value entries as Reactor's context doesn't allow null values.
        Map<Object, Object> contextValues = context.getValues().entrySet().stream()
                .filter(kvp -> kvp.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return CoreUtil.isNullOrEmpty(contextValues)
                ? reactor.util.context.Context.empty()
                : reactor.util.context.Context.of(contextValues);
    }
}
