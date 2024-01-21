package io.bipcrypto.kp01


public data class Mnemonic(val words: List<String>, val language: Language) {

    init {
        require(words.size == Strength.DEFAULT.wordCount) { "Must be 12 words but ${words.size} was found!" }
        require(
            WordList.validateWords(
                words,
                language
            )
        ) { "Unknown word(s) in ${language}. (${words.joinToString(", ")})" }
    }

    public fun toKeys(): Keys {
        val wordList = WordList.getDictionary(language)
        val out = IntArray(words.size)
        words.forEachIndexed { index, s -> out[index] = wordList.wordOf(s) }
        return Keys(out)
    }

    public override fun toString(): String = words.joinToString(", ")

}