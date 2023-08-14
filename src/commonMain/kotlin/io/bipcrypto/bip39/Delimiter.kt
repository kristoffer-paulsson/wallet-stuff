package io.bipcrypto.bip39

enum class Delimiter(val sep: Char) {
    ASCII(0x20.toChar()),
    JP(0x3000.toChar());

    companion object {
        fun find(language: Language) = when(language) {
            Language.JAPANESE -> JP.sep
            else -> ASCII.sep
        }

        fun find(sentence: String) = when {
            sentence.contains(ASCII.sep) -> ASCII.sep
            sentence.contains(JP.sep) -> JP.sep
            else -> error("There is no acceptable whitespace character in the Mnemonic sentence!")
        }
    }
}