package io.bipcrypto.crypto

import io.bipcrypto.util.readLongAt
import io.bipcrypto.util.swapEndian
import io.bipcrypto.util.writeLongAt


class Sha512Engine: HashEngine {

    override val type: Algorithm
        get() = Sha256Engine.TYPE

    private val h = longArrayOf(
        0x6A09E667F3BCC908L, -0x4498517a7b3558c5L, 0x3C6EF372FE94F82BL, -0x5ab00ac5a0e2c90fL,
        0x510E527FADE682D1L, -0x64fa9773d4c193e1L, 0x1F83D9ABFB41BD6BL, 0x5BE0CD19137E2179L
    )
    private val w = LongArray(k.size)

    private var lasting: ByteArray = ByteArray(0)

    private var count = 0

    private fun push(chunk: ByteArray) = (0 until 16).forEach { i ->
        w[i] = chunk.readLongAt(i * INT_LEN).swapEndian()
    }

    private fun push(block: LongArray) = block.copyInto(w, 0, 0, 16)

    private fun transform() {
        (16 until w.size).forEach { t ->
            w[t] = smallSig1(w[t-2]) + w[t-7] + smallSig0(w[t-15]) + w[t-16]
        }
        val temp = h.copyOf()
        w.indices.forEach { t ->
            val t1 = temp[7] + bigSig1(temp[4]) + ch(temp[4], temp[5], temp[6]) + k[t] + w[t]
            val t2 = bigSig0(temp[0]) + maj(temp[0], temp[1], temp[2])
            (temp.size - 1 downTo 1).forEach { j -> temp[j] = temp[j - 1] }
            temp[4] += t1
            temp[0] = t1 + t2
        }
        h.indices.forEach { t -> h[t] += temp[t] }
    }

    override fun update(messagePart: ByteArray) {
        val buffer = lasting + messagePart
        lasting = ByteArray(0) // Setting an empty array

        (0..buffer.size step BLK_LEN).forEach { i ->

            // Slicing the buffer in ranges of 64, if too small it's lasting.
            val chunk = try {
                messagePart.copyOfRange(i, i + BLK_LEN)
            } catch (_: IndexOutOfBoundsException) {
                lasting = buffer.copyOfRange(i, buffer.size)
                return
            }

            push(chunk)
            transform()

            count += BLK_LEN
        }
    }

    override fun final(): ByteArray {
        count += lasting.size
        val blocksNeeded = if (lasting.size + 1 + 16 > BLK_LEN) 2 else 1
        val blockLength = lasting.size / INT_LEN
        val end = LongArray(blocksNeeded * 16)

        (0 until blockLength).forEach { i ->
            end[i] = lasting.readLongAt(i * INT_LEN).swapEndian()
        }

        val remainder = (lasting.copyOfRange(
            blockLength * INT_LEN, lasting.size
        ) + 128.toByte()).copyOf(INT_LEN)
        end[blockLength] = remainder.readLongAt(0).swapEndian()

        val totalSize = count * 8L
        end[end.size - 1] = totalSize

        if (blocksNeeded == 1) {
            push(end)
            transform()
        } else {
            push(end.copyOfRange(0, 16))
            transform()
            push(end.copyOfRange(16, 32))
            transform()
        }

        val hash = ByteArray(h.size * INT_LEN)
        h.indices.forEach { hash.writeLongAt(it * INT_LEN, h[it].swapEndian()) }
        return hash
    }

    companion object: AlgorithmType {
        override val TYPE = Algorithm.SHA512
        override val BLK_LEN = TYPE.blkSize
        override val INT_LEN = TYPE.intSize
        override val OUT_SIZE = TYPE.hashSize

        private inline fun ch(x: Long, y: Long, z: Long): Long = x and y or (x.inv() and z)
        private inline fun maj(x: Long, y: Long, z: Long): Long = x and y or (x and z) or (y and z)
        private inline fun bigSig0(x: Long): Long = x.rotateRight(28) xor x.rotateRight(
            34) xor x.rotateRight(39)
        private inline fun bigSig1(x: Long): Long = x.rotateRight(14) xor x.rotateRight(
            18) xor x.rotateRight(41)
        private inline fun smallSig0(x: Long): Long = x.rotateRight(1) xor x.rotateRight(
            8) xor (x ushr 7)
        private inline fun smallSig1(x: Long): Long = x.rotateRight(19) xor x.rotateRight(
            61) xor (x ushr 6)

        private val k = longArrayOf(
            0x428A2F98D728AE22L, 0x7137449123EF65CDL, -0x4a3f043013b2c4d1L, -0x164a245a7e762444L,
            0x3956C25BF348B538L, 0x59F111F1B605D019L, -0x6dc07d5b50e6b065L, -0x54e3a12a25927ee8L,
            -0x27f855675cfcfdbeL, 0x12835B0145706FBEL, 0x243185BE4EE4B28CL, 0x550C7DC3D5FFB4E2L,
            0x72BE5D74F27B896FL, -0x7f214e01c4e9694fL, -0x6423f958da38edcbL, -0x3e640e8b3096d96cL,
            -0x1b64963e610eb52eL, -0x1041b879c7b0da1dL, 0x0FC19DC68B8CD5B5L, 0x240CA1CC77AC9C65L,
            0x2DE92C6F592B0275L, 0x4A7484AA6EA6E483L, 0x5CB0A9DCBD41FBD4L, 0x76F988DA831153B5L,
            -0x67c1aead11992055L, -0x57ce3992d24bcdf0L, -0x4ffcd8376704dec1L, -0x40a680384110f11cL,
            -0x391ff40cc257703eL, -0x2a586eb86cf558dbL, 0x06CA6351E003826FL, 0x142929670A0E6E70L,
            0x27B70A8546D22FFCL, 0x2E1B21385C26C926L, 0x4D2C6DFC5AC42AEDL, 0x53380D139D95B3DFL,
            0x650A73548BAF63DEL, 0x766A0ABB3C77B2A8L, -0x7e3d36d1b812511aL, -0x6d8dd37aeb7dcac5L,
            -0x5d40175eb30efc9cL, -0x57e599b443bdcfffL, -0x3db4748f2f07686fL, -0x3893ae5cf9ab41d0L,
            -0x2e6d17e62910ade8L, -0x2966f9dbaa9a56f0L, -0xbf1ca7aa88edfd6L, 0x106AA07032BBD1B8L,
            0x19A4C116B8D2D0C8L, 0x1E376C085141AB53L, 0x2748774CDF8EEB99L, 0x34B0BCB5E19B48A8L,
            0x391C0CB3C5C95A63L, 0x4ED8AA4AE3418ACBL, 0x5B9CCA4F7763E373L, 0x682E6FF3D6B2B8A3L,
            0x748F82EE5DEFB2FCL, 0x78A5636F43172F60L, -0x7b3787eb5e0f548eL, -0x7338fdf7e59bc614L,
            -0x6f410005dc9ce1d8L, -0x5baf9314217d4217L, -0x41065c084d3986ebL, -0x398e870d1c8dacd5L,
            -0x35d8c13115d99e64L, -0x2e794738de3f3df9L, -0x15258229321f14e2L, -0xa82b08011912e88L,
            0x06F067AA72176FBAL, 0x0A637DC5A2C898A6L, 0x113F9804BEF90DAEL, 0x1B710B35131C471BL,
            0x28DB77F523047D84L, 0x32CAAB7B40C72493L, 0x3C9EBE0A15C9BEBCL, 0x431D67C49C100D4CL,
            0x4CC5D4BECB3E42B6L, 0x597F299CFC657E2AL, 0x5FCB6FAB3AD6FAECL, 0x6C44198C4A475817L
        )
    }
}