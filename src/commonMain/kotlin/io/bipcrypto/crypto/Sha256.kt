package io.bipcrypto.crypto

import io.bipcrypto.util.readIntAt
import io.bipcrypto.util.swapEndian
import io.bipcrypto.util.writeIntAt


class Sha256Engine: HashEngine {

    override val type: Algorithm
        get() = TYPE

    private val h = intArrayOf(
        0x6a09e667, -0x4498517b, 0x3c6ef372, -0x5ab00ac6,
        0x510e527f, -0x64fa9774, 0x1f83d9ab, 0x5be0cd19
    )
    private val w = IntArray(k.size)

    private var count = 0

    private var lasting: ByteArray = ByteArray(0)

    private fun push(chunk: ByteArray) = (0 until 16).forEach { i ->
        w[i] = chunk.readIntAt(i * INT_LEN).swapEndian()
    }

    private fun push(block: IntArray) = block.copyInto(w, 0, 0, 16)

    private fun transform() {
        (16 until w.size).forEach { t ->
            w[t] = smallSig1(w[t - 2]) + w[t - 7] + smallSig0(w[t - 15]) + w[t - 16]
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
        val blocksNeeded = if (lasting.size + 1 + 8 > BLK_LEN) 2 else 1
        val blockLength = lasting.size / INT_LEN
        val end = IntArray(blocksNeeded * 16)

        (0 until blockLength).forEach { i ->
            end[i] = lasting.readIntAt(i * INT_LEN).swapEndian()
        }

        val remainder = (lasting.copyOfRange(
            blockLength * INT_LEN, lasting.size
        ) + 128.toByte()).copyOf(INT_LEN)
        end[blockLength] = remainder.readIntAt(0).swapEndian()

        val totalSize = count * 8L
        end[end.size - 2] = ((totalSize) ushr 32).toInt()
        end[end.size - 1] = totalSize.toInt()

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
        h.indices.forEach { hash.writeIntAt(it * INT_LEN, h[it].swapEndian()) }
        return hash
    }

    companion object: AlgorithmType {
        override val TYPE = Algorithm.SHA256
        override val BLK_LEN = TYPE.blkSize
        override val INT_LEN = TYPE.intSize
        override val OUT_SIZE = TYPE.hashSize

        private inline fun ch(x: Int, y: Int, z: Int): Int = x and y or (x.inv() and z)
        private inline fun maj(x: Int, y: Int, z: Int): Int = x and y or (x and z) or (y and z)
        private inline fun bigSig0(x: Int): Int = x.rotateRight(2) xor x.rotateRight(
            13) xor x.rotateRight(22)
        private inline fun bigSig1(x: Int): Int = x.rotateRight(6) xor x.rotateRight(
            11) xor x.rotateRight(25)
        private inline fun smallSig0(x: Int): Int = x.rotateRight(7) xor x.rotateRight(
            18) xor (x ushr 3)
        private inline fun smallSig1(x: Int): Int = x.rotateRight(17) xor x.rotateRight(
            19) xor (x ushr 10)

        private val k = intArrayOf(
            0x428a2f98, 0x71374491, -0x4a3f0431, -0x164a245b,
            0x3956c25b, 0x59f111f1, -0x6dc07d5c, -0x54e3a12b,
            -0x27f85568, 0x12835b01, 0x243185be, 0x550c7dc3,
            0x72be5d74, -0x7f214e02, -0x6423f959, -0x3e640e8c,
            -0x1b64963f, -0x1041b87a, 0x0fc19dc6, 0x240ca1cc,
            0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
            -0x67c1aeae, -0x57ce3993, -0x4ffcd838, -0x40a68039,
            -0x391ff40d, -0x2a586eb9, 0x06ca6351, 0x14292967,
            0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
            0x650a7354, 0x766a0abb, -0x7e3d36d2, -0x6d8dd37b,
            -0x5d40175f, -0x57e599b5, -0x3db47490, -0x3893ae5d,
            -0x2e6d17e7, -0x2966f9dc, -0xbf1ca7b, 0x106aa070,
            0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
            0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
            0x748f82ee, 0x78a5636f, -0x7b3787ec, -0x7338fdf8,
            -0x6f410006, -0x5baf9315, -0x41065c09, -0x398e870e
        )
    }
}