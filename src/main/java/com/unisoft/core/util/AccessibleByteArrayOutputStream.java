package com.unisoft.core.util;

import java.io.ByteArrayOutputStream;

/**
 * This class is an extension of {@link ByteArrayOutputStream} which allows access to the backing {@code byte[]} without
 * requiring a copying of the data. The only use of this class is for internal purposes where we know it is safe to
 * directly access the {@code byte[]} without copying.
 *
 * @author omar.H.Ajmi
 * @since 19/10/2020
 */
public class AccessibleByteArrayOutputStream extends ByteArrayOutputStream {
    @Override
    public synchronized byte[] toByteArray() {
        return super.buf;
    }
}
