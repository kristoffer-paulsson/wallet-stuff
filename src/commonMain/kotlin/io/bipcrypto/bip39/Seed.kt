package io.bipcrypto.bip39

import io.bipcrypto.util.BinHex
import kotlin.jvm.JvmInline

@JvmInline
value class Seed(val seed: ByteArray) {

    init {
        require(seed.size == 64)
    }

    fun toHex(): String = BinHex.encodeToHex(seed)
}