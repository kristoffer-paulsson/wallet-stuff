package io.bipcrypto.kp01

import org.angproj.crypt.hmac.KeyHashedMac
import org.angproj.crypt.sha.Sha512Hash

public class Kp01 internal constructor(
    public val entropy: Entropy,
    public val mnemonic: Mnemonic,
    public val passphrase: Passphrase,
) {

    init {
        require(entropy.entropy.size == Strength.DEFAULT.size)
        require(mnemonic.words.size == Strength.DEFAULT.wordCount)
    }

    public val seed: Seed by lazy { toSeed() }

    private fun toSeed(): Seed {
        val hmac = KeyHashedMac.create(passphrase.toSalt(), Sha512Hash)
        hmac.update(mnemonic.language.toByteArray() + entropy.entropy)
        return Seed(hmac.final().copyOf(64))
    }

    public constructor(
        entropy: Entropy,
        passphrase: Passphrase,
        language: Language = Language.ENGLISH,
    ) : this(entropy, entropy.toKeys().toMnemonic(language), passphrase)

    public constructor(
        mnemonic: Mnemonic,
        passphrase: Passphrase,
    ) : this(mnemonic.toKeys().toEntropy(), mnemonic, passphrase)

    public companion object {

        public fun fromSentence(sentence: String, passphrase: String): Kp01 {
            val words = mutableListOf<String>()
            sentence.split(',').forEach { words.add(it.trim()) }
            val language = WordList.recognize(words)
            return Kp01(Mnemonic(words, language), Passphrase(passphrase))
        }

        public fun fromSentence(sentence: String, language: Language, passphrase: String): Kp01 {
            val words = mutableListOf<String>()
            sentence.split(',').forEach { words.add(it.trim()) }
            return Kp01(Mnemonic(words, language), Passphrase(passphrase))
        }
    }
}