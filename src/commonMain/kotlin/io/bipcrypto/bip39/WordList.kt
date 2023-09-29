package io.bipcrypto.bip39

public sealed interface WordList {

    public val wordList: List<String>

    public fun wordOf(word: String): Int = when (val index = wordList.indexOf(word)) {
        -1 -> throw IllegalArgumentException("Word not in list. ($word - $this)")
        else -> index
    }

    public fun wordAt(index: Int): String = wordList.elementAt(index)

    public companion object {
        public fun getDictionary(language: Language): WordList = when (language) {
            Language.ENGLISH -> DictionaryEng
            Language.JAPANESE -> DictionaryJpn
            Language.KOREAN -> DictionaryKor
            Language.SPANISH -> DictionarySpa
            Language.CHINESE_SIMPLIFIED -> DictionaryChiSimplified
            Language.CHINESE_TRADITIONAL -> DictionaryChiTraditional
            Language.FRENCH -> DictionaryFre
            Language.ITALIAN -> DictionaryIta
            Language.CZECH -> DictionstyCze
            Language.PORTUGUESE -> DictionaryPor
        }

        public fun validateWords(words: List<String>, language: Language): Boolean = getDictionary(language).wordList.containsAll(words)

        public fun recognize(words: List<String>): Language = when {
            validateWords(words, Language.ENGLISH) -> Language.ENGLISH
            validateWords(words, Language.JAPANESE) -> Language.JAPANESE
            validateWords(words, Language.KOREAN) -> Language.KOREAN
            validateWords(words, Language.SPANISH) -> Language.SPANISH
            validateWords(words, Language.CHINESE_SIMPLIFIED) -> Language.CHINESE_SIMPLIFIED
            validateWords(words, Language.CHINESE_TRADITIONAL) -> Language.CHINESE_TRADITIONAL
            validateWords(words, Language.FRENCH) -> Language.FRENCH
            validateWords(words, Language.ITALIAN) -> Language.ITALIAN
            validateWords(words, Language.CZECH) -> Language.CZECH
            validateWords(words, Language.PORTUGUESE) -> Language.PORTUGUESE
            else -> error("Invalid word(s) or unsupported language!")
        }
    }
}