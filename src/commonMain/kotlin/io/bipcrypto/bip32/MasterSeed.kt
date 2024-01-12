package io.bipcrypto.bip32

import io.bipcrypto.util.BinHex
import kotlin.jvm.JvmInline

@JvmInline
public value class MasterSeed(private val value: ByteArray) {

    init {
        require(value.size in MIN_SIZE..MAX_SIZE) {
            "Illegal size on master seed for a BIP32 master key." }
    }

    public val seed: ByteArray
        get() = value.copyOf()

    public fun toHex(): String = BinHex.encodeToHex(value)

    public companion object {
        public const val MIN_SIZE: Int = 128 / Byte.SIZE_BITS
        public const val MAX_SIZE: Int = 512 / Byte.SIZE_BITS
    }
}