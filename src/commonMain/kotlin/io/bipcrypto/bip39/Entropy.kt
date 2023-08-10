package io.bipcrypto.bip39

import io.bipcrypto.crypto.HASH
import kotlin.jvm.JvmInline

@JvmInline
value class Entropy (val entropy: ByteArray) {

    init {
        require(sizes.contains(entropy.size))
    }

    fun toKeys(): Keys {
        val strength = Strength.bySize(entropy.size)
        val mnemonic = IntArray(strength.count)
        (0 until mnemonic.size - 1).forEachIndexed { index, i -> mnemonic[index] = extractWord(entropy, index) }
        mnemonic[mnemonic.size - 1] = extractLast(entropy, strength.count - 1)
        return Keys(mnemonic)
    }

    companion object {
        val sizes = setOf(
            Strength.DEFAULT.size,
            Strength.LOW.size,
            Strength.MEDIUM.size,
            Strength.HIGH.size,
            Strength.VERY_HIGH.size
        )

        private fun extract1(e: ByteArray, o: Int): Int = (
                (e[o+0].toInt() and 0x000000ff) shl 3) or (
                (e[o+1].toInt() and 0x000000e0) shr 5)

        private fun extract2(e: ByteArray, o: Int): Int = (
                (e[o+1].toInt() and 0x0000001f) shl 6) or (
                (e[o+2].toInt() and 0x000000fc) shr 2)

        private fun extract3(e: ByteArray, o: Int): Int = (
                (e[o+2].toInt() and 0x00000003) shl 9) or (
                (e[o+3].toInt() and 0x000000ff) shl 1) or (
                (e[o+4].toInt() and 0x00000080) shr 7)

        private fun extract4(e: ByteArray, o: Int): Int = (
                (e[o+4].toInt() and 0x0000007f) shl 4) or (
                (e[o+5].toInt() and 0x000000f0) shr 4)

        private fun extract5(e: ByteArray, o: Int): Int = (
                (e[o+5].toInt() and 0x0000000f) shl 7) or (
                (e[o+6].toInt() and 0x000000fe) shr 1)

        private fun extract6(e: ByteArray, o: Int): Int = (
                (e[o+6].toInt() and 0x00000001) shl 10) or (
                (e[o+7].toInt() and 0x000000ff) shl 2) or (
                (e[o+8].toInt() and 0x000000c0) shr 6)

        private fun extract7(e: ByteArray, o: Int): Int = (
                (e[o+8].toInt() and 0x0000003f) shl 5) or (
                (e[o+9].toInt() and 0x000000f8) shr 3)

        private fun extract8(e: ByteArray, o: Int): Int = (
                (e[o+9].toInt() and 0x00000007) shl 8) or (
                e[o+10].toInt() and 0x000000ff)

        fun extractWord(entropy: ByteArray, wordIdx: Int): Int {
            val offset = wordIdx / 8 * 11
            val extr = wordIdx.mod(8)

            return when (extr) {
                0 -> extract1(entropy, offset)
                1 -> extract2(entropy, offset)
                2 -> extract3(entropy, offset)
                3 -> extract4(entropy, offset)
                4 -> extract5(entropy, offset)
                5 -> extract6(entropy, offset)
                6 -> extract7(entropy, offset)
                7 -> extract8(entropy, offset)
                else -> error("Not happening.")
            }
        }

        private fun extract12(e: ByteArray, o: Int): Int = (
                (e[o+4].toInt() and 0x0000007f) shl 4)

        private fun extract15(e: ByteArray, o: Int): Int = (
                (e[o+8].toInt() and 0x0000003f) shl 5)

        private fun extract18(e: ByteArray, o: Int): Int = (
                (e[o+1].toInt() and 0x0000001f) shl 6)

        private fun extract21(e: ByteArray, o: Int): Int = (
                (e[o+5].toInt() and 0x0000000f) shl 7)

        private fun extract24(e: ByteArray, o: Int): Int = (
                (e[o+9].toInt() and 0x00000007) shl 8)

        fun extractLast(entropy: ByteArray, wordIdx: Int): Int {
            val offset = wordIdx / 8 * 11
            val hash = HASH.sha256(entropy).raw

            return when (wordIdx) {
                11 -> extract12(entropy, offset) or ((hash[0].toInt() and 0x000000f0) shr 4)
                14 -> extract15(entropy, offset) or ((hash[0].toInt() and 0x000000f8) shr 3)
                17 -> extract18(entropy, offset) or ((hash[0].toInt() and 0x000000fc) shr 2)
                20 -> extract21(entropy, offset) or ((hash[0].toInt() and 0x000000fe) shr 1)
                23 -> extract24(entropy, offset) or (hash[0].toInt() and 0x000000ff)
                else -> error("Not happening.")
            }
        }
    }
}