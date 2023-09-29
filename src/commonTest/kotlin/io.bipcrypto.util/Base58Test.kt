package io.bipcrypto.util

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


class Base58Test {

    // Test vectors according to:
    // https://digitalbazaar.github.io/base58-spec/
    val vectors = listOf(
        "Hello World!".encodeToByteArray() to "2NEpo7TZRRrLZSi2U",
        "The quick brown fox jumps over the lazy dog.".encodeToByteArray() to "USm3fpXnKG5EUBx2ndxBDMPVciP5hGey2Jh4NDv6gmeo1LkMeiKrLJUUBk6Z"
    )

    @Test
    fun encode() {
        assertEquals(Base58.encode(vectors.get(0).first), vectors.get(0).second)
        assertEquals(Base58.encode(vectors.get(1).first), vectors.get(1).second)
    }

    @Test
    fun decode() {
        assertContentEquals(Base58.decode(vectors.get(0).second), vectors.get(0).first)
        assertContentEquals(Base58.decode(vectors.get(1).second), vectors.get(1).first)
    }
}