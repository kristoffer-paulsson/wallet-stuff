package io.bipcrypto.crypto

import kotlin.jvm.JvmInline

@JvmInline
value class HmacKey(private val prepared: Triple<ByteArray, ByteArray, Algorithm>) {

    init {
        require(prepared.first.size == algo.blkSize)
        require(prepared.second.size == algo.blkSize)
    }

    val algo: Algorithm
        get() = prepared.third

    val iPadKey: ByteArray
        get() = prepared.first

    val oPadKey: ByteArray
        get() = prepared.second
}