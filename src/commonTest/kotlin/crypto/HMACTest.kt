package io.bipcrypto.crypto

import vect.HMAC_SHA256Msg
import vect.HMAC_SHA512Msg
import kotlin.test.Test
import kotlin.test.assertContains


class HMACTest{

    @Test
    fun sha256Short() {
        HMAC_SHA256Msg.msgIter(HMAC_SHA256Msg.testVectors) { msg, key, md ->
            assertContains(HMAC.sha256(key, msg).toHex().lowercase(), md.lowercase())
        }
    }

    @Test
    fun sha512Short() {
        HMAC_SHA512Msg.msgIter(HMAC_SHA512Msg.testVectors) { msg, key, md ->
            assertContains(HMAC.sha512(key, msg).toHex().lowercase(), md.lowercase())
        }
    }

    @Test
    fun sha256Long() {
        HMAC_SHA256Msg.msgIter(HMAC_SHA256Msg.testVectors) { msg, key, md ->
            var done = false
            val digest = HMAC.sha256(HMAC.sha256Key(key)) {
                if (done) {
                    ByteArray(0)
                } else {
                    done = true
                    msg
                }
            }
            assertContains(digest.toHex().lowercase(), md.lowercase())
        }
    }

    @Test
    fun sha512Long() {
        HMAC_SHA512Msg.msgIter(HMAC_SHA512Msg.testVectors) { msg, key, md ->
            var done = false
            val digest = HMAC.sha512(HMAC.sha512Key(key)) {
                if (done) {
                    ByteArray(0)
                } else {
                    done = true
                    msg
                }
            }
            assertContains(digest.toHex().lowercase(), md.lowercase())
        }
    }
}