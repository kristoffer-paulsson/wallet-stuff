package io.bipcrypto.kp01

public sealed interface WordList {

    public val wordList: List<String>

    public fun normalize(word: String): String = word

    public fun wordOf(word: String): Int = when (val index = wordList.indexOf(normalize(word))) {
        -1 -> throw IllegalArgumentException("Word not in list. ($word - $this)")
        else -> index
    }

    public fun wordAt(index: Int): String = wordList.elementAt(index)

    public companion object {
        public fun getDictionary(language: Language): WordList = when (language) {
            Language.ENGLISH -> DictionaryEng
            else -> error("Unsupported language!")
        }

        public fun validateWords(words: List<String>, language: Language): Boolean = getDictionary(language).wordList.containsAll(words)

        public fun recognize(words: List<String>): Language = when {
            validateWords(words, Language.ENGLISH) -> Language.ENGLISH
            else -> error("Invalid word(s) or unsupported language!")
        }
    }
}