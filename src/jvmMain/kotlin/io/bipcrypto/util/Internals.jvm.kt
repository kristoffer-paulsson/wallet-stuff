package io.bipcrypto.util

import java.security.MessageDigest

actual class Internals {

    actual companion object {

        val sha256Digest: MessageDigest = MessageDigest.getInstance("SHA-256")

        init {
            checkNotNull(sha256Digest) { "SHA-256 not found!" }
        }

        actual fun sha256(bytes: ByteArray): ByteArray = sha256Digest.digest(bytes)
    }
}