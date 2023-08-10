package io.bipcrypto.crypto

interface Engine {

    fun update(messagePart: ByteArray)

    fun final(): ByteArray
}