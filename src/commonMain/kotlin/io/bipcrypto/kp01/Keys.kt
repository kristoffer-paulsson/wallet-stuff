package io.bipcrypto.kp01

import org.angproj.crypt.sha.Sha512Hash
import kotlin.jvm.JvmInline

@JvmInline
public value class Keys(private val mnemonic: IntArray) {

    init {
        require(mnemonic.size == Strength.DEFAULT.wordCount)
    }

    public fun toMnemonic(language: Language): Mnemonic {
        val wordList = WordList.getDictionary(language)
        val words = mutableListOf<String>()
        mnemonic.forEach { i -> words.add(wordList.wordAt(i)) }
        return Mnemonic(words, language)
    }

    public fun toEntropy(): Entropy {
        val entropy = ByteArray(16)
        (entropy.indices).forEachIndexed { index, _ -> entropy[index] = extractByte(mnemonic, index) }
        val sha = Sha512Hash.create()
        sha.update(entropy)
        check(verifyChecksum(sha.final(), mnemonic.last())) { "Mnemonic checksum validation failure." }
        return Entropy(entropy)
    }

    protected companion object {

        private fun extract1(m: IntArray, o: Int): Byte = (m[o + 0] shr 3).toByte()

        private fun extract2(m: IntArray, o: Int): Byte = ((
                (m[o + 0] and 0x00000007) shl 5) or (
                (m[o + 1] and 0x000007c0) shr 6)).toByte()

        private fun extract3(m: IntArray, o: Int): Byte = ((
                (m[o + 1] and 0x0000003f) shl 2) or (
                (m[o + 2] and 0x00000600) shr 9)).toByte()

        private fun extract4(m: IntArray, o: Int): Byte = (
                (m[o + 2] and 0x000001fe) shr 1).toByte()

        private fun extract5(m: IntArray, o: Int): Byte = ((
                (m[o + 2] and 0x00000001) shl 7) or (
                (m[o + 3] and 0x000007f0) shr 4)).toByte()

        private fun extract6(m: IntArray, o: Int): Byte = ((
                (m[o + 3] and 0x0000000f) shl 4) or (
                (m[o + 4] and 0x00000780) shr 7)).toByte()

        private fun extract7(m: IntArray, o: Int): Byte = ((
                (m[o + 4] and 0x0000007f) shl 1) or (
                (m[o + 5] and 0x00000400) shr 10)).toByte()

        private fun extract8(m: IntArray, o: Int): Byte = (
                (m[o + 5] and 0x000003fc) shr 2).toByte()

        private fun extract9(m: IntArray, o: Int): Byte = ((
                (m[o + 5] and 0x00000003) shl 6) or (
                (m[o + 6] and 0x000007e0) shr 5)).toByte()

        private fun extract10(m: IntArray, o: Int): Byte = ((
                (m[o + 6] and 0x0000001f) shl 3) or (
                (m[o + 7] and 0x00000700) shr 8)).toByte()

        private fun extract11(m: IntArray, o: Int): Byte = (
                m[o + 7] and 0x000000ff).toByte()

        private fun extractByte(mnemonic: IntArray, byteIdx: Int): Byte {
            val offset = byteIdx / 11 * 8
            val extr = byteIdx.mod(11)

            return when (extr) {
                0 -> extract1(mnemonic, offset)
                1 -> extract2(mnemonic, offset)
                2 -> extract3(mnemonic, offset)
                3 -> extract4(mnemonic, offset)
                4 -> extract5(mnemonic, offset)
                5 -> extract6(mnemonic, offset)
                6 -> extract7(mnemonic, offset)
                7 -> extract8(mnemonic, offset)
                8 -> extract9(mnemonic, offset)
                9 -> extract10(mnemonic, offset)
                10 -> extract11(mnemonic, offset)
                else -> error("Not happening.")
            }
        }

        private fun verifyChecksum(hash: ByteArray, lastWord: Int): Boolean {
            return (lastWord and 0x00000000f) == ((hash[0].toInt() shr 4) and 0x00000000f)
        }
    }
}