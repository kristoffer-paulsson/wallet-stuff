package io.bipcrypto.util

expect class Internals {
    companion object {
        fun sha256(bytes: ByteArray): ByteArray
    }
}