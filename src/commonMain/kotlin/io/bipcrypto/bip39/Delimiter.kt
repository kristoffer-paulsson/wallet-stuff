package io.bipcrypto.bip39

public enum class Delimiter(public val sep: Char) {
    ASCII(0x20.toChar()),
    JP(0x3000.toChar());

    public companion object {
        public fun find(language: Language): Char = when(language) {
            Language.JAPANESE -> JP.sep
            else -> ASCII.sep
        }

        public fun find(sentence: String): Char = when {
            sentence.contains(ASCII.sep) -> ASCII.sep
            sentence.contains(JP.sep) -> JP.sep
            else -> error("There is no acceptable whitespace character in the Mnemonic sentence!")
        }
    }
}