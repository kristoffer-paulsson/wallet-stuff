package io.bipcrypto.bip32

import org.angproj.aux.num.BigInt
import org.angproj.aux.num.BigSigned
import org.angproj.aux.util.EndianAware
import org.angproj.aux.util.bigIntOf
import org.angproj.aux.util.writeUIntAt
import org.angproj.crypt.sec.Conversion
import org.angproj.crypt.sec.EllipticCurvePoint
import org.angproj.crypt.sec.FieldElement
import org.angproj.crypt.sec.Secp256Koblitz1

/**
 * https://www.secg.org/sec2-v2.pdf
 *
 * Complementary specification to BIP-0032:
 * https://www.secg.org/sec1-v2.pdf
 *
 * SEC1 V2, section 2.3
 * */

/**
 * https://github.com/horizontalsystems/ethereum-kit-android
 * https://github.com/horizontalsystems/hd-wallet-kit-android
 * https://github.com/trustwallet/wallet-connect-kotlin
 * https://github.com/bcgit/bc-java
 * */

public object Bip32: EndianAware {
    public fun parse(p: BigInt): Pair<BigInt, BigInt> {
        TODO("FIX")
    }

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
        val point = EllipticCurvePoint(FieldElement(x), FieldElement(y))
        return Conversion.ellipticCurvePoint2octetString(point, Secp256Koblitz1.domainParameters, true).value
    }

    private val PREFIX_POSITIVE: ByteArray = byteArrayOf(BigSigned.POSITIVE.signed.toByte())
    private val HARDENED_FLAG: UInt = 0x80000000u
}