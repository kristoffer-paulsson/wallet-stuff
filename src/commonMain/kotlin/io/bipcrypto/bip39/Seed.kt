package io.bipcrypto.bip39

import io.bipcrypto.util.BinHex
import kotlin.jvm.JvmInline

@JvmInline
public value class Seed(private val seed: ByteArray) {

    init {
        require(seed.size == 64)
    }

    public fun toHex(): String = BinHex.encodeToHex(seed)
}