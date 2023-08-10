package io.bipcrypto.crypto

import io.bipcrypto.util.swapEndian
import io.bipcrypto.util.writeIntAt
import kotlin.math.ceil


class Pbkdf2Derive(private val algo: Algorithm, val kSize: Int, val count: Int = 1000): Derive {

    init {
        require(kSize >= 14) { "Key length to small" }
        require(kSize < Int.MAX_VALUE - 1) { "Key length to big" }
    }

    val len = ceil(kSize.toDouble() / algo.hashSize.toDouble()).toInt()
    val r = kSize- (len - 1) * algo.hashSize

    fun masterKey(password: ByteArray, salt: ByteArray): ByteArray {
        //require(salt.size >= 16) { "The salt must be at least 128 bits" }

        val mk = ByteArray(kSize)
        val prfPrep = HmacEngine.prepareHmacKey(password, algo)

        (1..len).forEach { i ->
            val t = ByteArray(algo.hashSize)
            var u = initU(salt, i)
            (1..count).forEach { _ ->
                u = prf(prfPrep, u)
                t.indices.forEach { k -> t[k] = (t[k].toInt() xor u[k].toInt()).toByte() }
            }
            t.copyInto(mk, (i-1) * algo.hashSize, 0, r)
        }
        return mk.copyOfRange(0, kSize)
    }

    private fun prf(prep: HmacKey, u: ByteArray): ByteArray {
        val prf = HmacEngine(prep)
        prf.update(u)
        return prf.final()
    }

    private fun initU(s: ByteArray, i: Int): ByteArray {
        val u = ByteArray(s.size + Int.SIZE_BYTES)
        s.copyInto(u)
        u.writeIntAt(s.size, i.swapEndian())
        return u
    }
}