package io.bipcrypto.bip39

import doist.x.normalize.Form
import doist.x.normalize.normalize
import org.angproj.crypt.kdf.PasswordBasedKdf2
import org.angproj.crypt.sha.Sha512Hash


data class Mnemonic(val words: List<String>, val language: Language) {

    init {
        require(Strength.counts.contains(words.size)) { "Must be 12, 15, 18, 21 or 24 words but ${words.size} was found!" }
        require(WordList.validateWords(words, language)) { "Unknown word(s) in ${language}. (${words.joinToString(" ")})" }
    }

    fun toKeys(): Keys {
        val wordList = WordList.getDictionary(language)
        val out = IntArray(words.size)
        words.forEachIndexed { index, s -> out[index] = wordList.wordOf(s) }
        return Keys(out)
    }

    fun toSentence(): ByteArray {
        return words.joinToString(Delimiter.find(language).toString()).normalize(Form.NFKD).encodeToByteArray()
    }
}

fun Mnemonic.toSeed(passphrase: Passphrase): Seed {
    val password = this.toSentence()
    val salt = passphrase.toSalt()

    val kdf = PasswordBasedKdf2.create(Sha512Hash, 64, 2048)
    return Seed(kdf.newKey(password, salt))
}