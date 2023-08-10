package io.bipcrypto.crypto

import vect.SHA256LongMsg
import vect.SHA256ShortMsg
import vect.SHA512LongMsg
import vect.SHA512ShortMsg
import kotlin.test.Test
import kotlin.test.assertContains


class HASHTest {

    @Test
    fun testSha256Short() {
        SHA256ShortMsg.msgIter(SHA256ShortMsg.testVectors) { msg, md ->
            println("testSha256Short: $md")
            assertContains(HASH.sha256(msg).toHex().lowercase(), md.lowercase())
        }
    }

    @Test
    fun testSha512Short() {
        SHA512ShortMsg.msgIter(SHA512ShortMsg.testVectors) { msg, md ->
            println("testSha512Short: $md")
            assertContains(HASH.sha512(msg).toHex().lowercase(), md.lowercase())
        }
    }

    @Test
    fun testSha256Long() {
        SHA256LongMsg.msgIter(SHA256LongMsg.testVectors) { msg, md ->
            var done = false
            val digest = HASH.sha256 {
                if (done) {
                    ByteArray(0)
                } else {
                    done = true
                    msg
                }
            }
            println("testSha256Long: $md")
            assertContains(digest.toHex().lowercase(), md.lowercase())
        }
    }

    @Test
    fun testSha512Long() {
        SHA512LongMsg.msgIter(SHA512LongMsg.testVectors) { msg, md ->
            var done = false
            val digest = HASH.sha512 {
                if (done) {
                    ByteArray(0)
                } else {
                    done = true
                    msg
                }
            }
            println("testSha512Long: $md")
            assertContains(digest.toHex().lowercase(), md.lowercase())
        }
    }
}