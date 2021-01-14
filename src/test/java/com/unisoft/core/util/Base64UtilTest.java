package com.unisoft.core.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Base64UtilTest {
    @Test
    void testEncodeAndDecode() {
        byte[] src = new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81};
        byte[] dst = Base64Util.encode(src);
        assertTrue(Arrays.equals(Base64Util.decode(dst), src));
    }

    @Test
    void testEncodeNullValue() {
        assertNull(Base64Util.encode(null));
    }

    @Test
    void testDecodeNullValue() {
        assertNull(Base64Util.decode(null));
    }

    @Test
    void testDecodeString() {
        byte[] src = new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81};
        String dstString = Base64Util.encodeToString(src);
        assertTrue(Arrays.equals(Base64Util.decodeString(dstString), src));
    }

    @Test
    void testDecodeStringNullValue() {
        assertNull(Base64Util.decodeString(null));
    }

    @Test
    void testEncodeURLWithoutPaddingNullValue() {
        assertNull(Base64Util.encodeURLWithoutPadding(null));
    }

    @Test
    void testDecodeURLNullValue() {
        assertNull(Base64Util.decodeURL(null));
    }
}