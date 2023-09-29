package io.bipcrypto.bip39

import doist.x.normalize.Form
import doist.x.normalize.normalize

public class Bip39 internal constructor(
    public val entropy: Entropy,
    public val mnemonic: Mnemonic,
    public val passphrase: Passphrase,
    public val strength: Strength
) {

    init {
        require(entropy.entropy.size == strength.size)
        require(mnemonic.words.size == strength.wordCount)
    }

    public val seed: Seed by lazy { mnemonic.toSeed(passphrase) }

    public constructor(
        entropy: Entropy,
        passphrase: Passphrase = Passphrase(),
        language: Language = Language.ENGLISH,
    ) : this(entropy, entropy.toKeys().toMnemonic(language), passphrase, Strength.bySize(entropy.entropy.size))

    public constructor(
        mnemonic: Mnemonic,
        passphrase: Passphrase = Passphrase(),
    ) : this(mnemonic.toKeys().toEntropy(), mnemonic, passphrase, Strength.byCount(mnemonic.words.size))

    public companion object {
        public fun fromSentence(sentence: String, passphrase: String = ""): Bip39 {
            val normalized = sentence.normalize(Form.NFKD)
            val sep = Delimiter.find(normalized)
            val words = mutableListOf<String>()
            normalized.split(sep).forEach { words.add(it.trim()) }
            val language = WordList.recognize(words)

            return Bip39(Mnemonic(words, language), Passphrase(passphrase))
        }

        public fun fromSentence(sentence: String, language: Language, passphrase: String = ""): Bip39 {
            val normalized = sentence.normalize(Form.NFKD)
            val sep = Delimiter.find(normalized)
            val words = mutableListOf<String>()
            normalized.split(sep).forEach { words.add(it.trim()) }
            return Bip39(Mnemonic(words, language), Passphrase(passphrase))
        }
    }
}