package io.bipcrypto.kp01

import io.bipcrypto.bip32.MasterSeed
import io.bipcrypto.util.BinHex
import kotlin.jvm.JvmInline

@JvmInline
public value class Seed(private val value: ByteArray) {

    init {
        require(value.size == 64)
    }

    public val seed: ByteArray
        get() = value.copyOf()

    public fun toHex(): String = BinHex.encodeToHex(value)
}

public fun Seed.toBip32MasterSeed(): MasterSeed = MasterSeed(seed)