package io.bipcrypto.bip32

import org.angproj.aux.num.BigInt
import org.angproj.aux.num.BigSigned
import org.angproj.aux.pkg.Convention.Companion.asBig
import org.angproj.aux.util.bigIntOf
import org.angproj.aux.util.writeUIntAt

public object Bip32 {
    public fun parse256(p: ByteArray) : BigInt = when(p.first().toInt() < 0) {
        true -> bigIntOf(PREFIX_POSITIVE + p)
        else -> bigIntOf(p)
    }

    public fun serialize32(i: UInt): ByteArray = ByteArray(4).also { it.writeUIntAt(0, i.asBig()) }

    public fun serialize256(p: BigInt): ByteArray {
        val serial = p.toByteArray()
        return when {
            serial.size > 32 -> serial.copyOfRange(serial.size - 32, serial.size)
            serial.size < 32 -> ByteArray(32).also {
                it.fill(BigSigned.POSITIVE.signed.toByte())
                serial.copyInto(it, 32 - serial.size, 0, serial.size)
            }
            else -> serial
        }
    }

    public fun serializePair(x: BigInt, y: BigInt): ByteArray {
        TODO("FIX")
    }

    private val PREFIX_POSITIVE: ByteArray = byteArrayOf(BigSigned.POSITIVE.signed.toByte())
    private val HARDENED_FLAG: UInt = 0x80000000u
}