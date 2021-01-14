package com.unisoft.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base64UrlTest {
    private static void assertEmptyString(String input) {
        assertEquals("", input);
    }

    @Test
    void constructorWithNullBytes() {
        final Base64Url base64Url = new Base64Url((byte[]) null);
        assertNull(base64Url.encodedBytes());
        assertNull(base64Url.decodedBytes());
        assertEmptyString(base64Url.toString());
    }

    @Test
    void constructorWithEmptyBytes() {
        final Base64Url base64Url = new Base64Url(new byte[0]);
        assertArrayEquals(new byte[0], base64Url.encodedBytes());
        assertArrayEquals(new byte[0], base64Url.decodedBytes());
        assertEquals("", base64Url.toString());
    }

    @Test
    void constructorWithNonEmptyBytes() {
        final Base64Url base64Url = new Base64Url(new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81});
        assertArrayEquals(new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81}, base64Url.encodedBytes());
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, base64Url.decodedBytes());
        assertEquals("AAECAwQFBgcICQ", base64Url.toString());
    }

    @Test
    void constructorWithNullString() {
        final Base64Url base64Url = new Base64Url((String) null);
        assertNull(base64Url.encodedBytes());
        assertNull(base64Url.decodedBytes());
        assertEmptyString(base64Url.toString());
    }

    @Test
    void constructorWithEmptyString() {
        final Base64Url base64Url = new Base64Url("");
        assertArrayEquals(new byte[0], base64Url.encodedBytes());
        assertArrayEquals(new byte[0], base64Url.decodedBytes());
        assertEquals("", base64Url.toString());
    }

    @Test
    void constructorWithEmptyDoubleQuotedString() {
        final Base64Url base64Url = new Base64Url("\"\"");
        assertArrayEquals(new byte[0], base64Url.encodedBytes());
        assertArrayEquals(new byte[0], base64Url.decodedBytes());
        assertEquals("", base64Url.toString());
    }

    @Test
    void constructorWithEmptySingleQuotedString() {
        final Base64Url base64Url = new Base64Url("''");
        assertArrayEquals(new byte[0], base64Url.encodedBytes());
        assertArrayEquals(new byte[0], base64Url.decodedBytes());
        assertEquals("", base64Url.toString());
    }

    @Test
    void constructorWithNonEmptyString() {
        final Base64Url base64Url = new Base64Url("AAECAwQFBgcICQ");
        assertArrayEquals(new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81}, base64Url.encodedBytes());
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, base64Url.decodedBytes());
        assertEquals("AAECAwQFBgcICQ", base64Url.toString());
    }

    @Test
    void constructorWithNonEmptyDoubleQuotedString() {
        final Base64Url base64Url = new Base64Url("\"AAECAwQFBgcICQ\"");
        assertArrayEquals(new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81}, base64Url.encodedBytes());
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, base64Url.decodedBytes());
        assertEquals("AAECAwQFBgcICQ", base64Url.toString());
    }

    @Test
    void constructorWithNonEmptySingleQuotedString() {
        final Base64Url base64Url = new Base64Url("'AAECAwQFBgcICQ'");
        assertArrayEquals(new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81}, base64Url.encodedBytes());
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, base64Url.decodedBytes());
        assertEquals("AAECAwQFBgcICQ", base64Url.toString());
    }

    @Test
    void encodeWithNullBytes() {
        final Base64Url base64Url = Base64Url.encode(null);
        assertNull(base64Url.encodedBytes());
        assertNull(base64Url.decodedBytes());
        assertEmptyString(base64Url.toString());
    }

    @Test
    void encodeWithEmptyBytes() {
        final Base64Url base64Url = Base64Url.encode(new byte[0]);
        assertArrayEquals(new byte[0], base64Url.encodedBytes());
        assertArrayEquals(new byte[0], base64Url.decodedBytes());
        assertEquals("", base64Url.toString());
    }

    @Test
    void encodeWithNonEmptyBytes() {
        final Base64Url base64Url = Base64Url.encode(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertArrayEquals(new byte[]{65, 65, 69, 67, 65, 119, 81, 70, 66, 103, 99, 73, 67, 81}, base64Url.encodedBytes());
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, base64Url.decodedBytes());
        assertEquals("AAECAwQFBgcICQ", base64Url.toString());
    }
}