package io.bipcrypto.bip39

sealed interface WordList {

    val delimiter: Char

    val wordList: List<String>

    fun wordOf(word: String): Int = when (val index = wordList.indexOf(word)) {
        -1 -> throw IllegalArgumentException("Word not in list.")
        else -> index
    }

    fun wordAt(index: Int): String = wordList.elementAt(index)

    companion object {
        fun getDictionary(language: Language): WordList = when (language) {
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
    }
}