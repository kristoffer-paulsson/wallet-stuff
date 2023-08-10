package io.bipcrypto.bip39

import kotlin.jvm.JvmInline


@JvmInline
value class Mnemonic(val words: List<String>) {

    init {
        require(sizes.contains(words.size))
    }

    fun toKeys(language: Language): Keys {
        val wordList = WordList.getDictionary(language)
        val out = IntArray(words.size)
        words.forEachIndexed { index, s -> out[index] = wordList.wordOf(s) }
        return Keys(out)
    }

    override fun toString(): String = words.joinToString(" ")

    companion object {
        val sizes = setOf(
            Strength.DEFAULT.count,
            Strength.LOW.count,
            Strength.MEDIUM.count,
            Strength.HIGH.count,
            Strength.VERY_HIGH.count
        )
    }
}