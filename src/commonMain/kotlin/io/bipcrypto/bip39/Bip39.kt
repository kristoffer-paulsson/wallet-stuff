package io.bipcrypto.bip39

import io.bipcrypto.crypto.PBKDF2

class Bip39 internal constructor(
    val entropy: Entropy,
    val mnemonic: Mnemonic,
    val passphrase: Passphrase,
    val language: Language,
    val strength: Strength
) {

    init {
        require(entropy.entropy.size == strength.size)
        require(mnemonic.words.size == strength.count)
        require(WordList.getDictionary(language).wordList.containsAll(mnemonic.words))
    }

    val seed: Seed by lazy { mnemonicToSeed(mnemonic, passphrase) }

    constructor(
        entropy: Entropy,
        passphrase: Passphrase = Passphrase(),
        language: Language = Language.ENGLISH,
        strength: Strength = Strength.DEFAULT
    ) : this(entropy, entropy.toKeys().toMnemonic(language), passphrase, language, strength)

    constructor(
        mnemonic: Mnemonic,
        passphrase: Passphrase = Passphrase(),
        language: Language = Language.ENGLISH,
        strength: Strength = Strength.DEFAULT
    ) : this(mnemonic.toKeys(language).toEntropy(), mnemonic, passphrase, language, strength)

    companion object {
        const val radix = 2048
        const val kLen = 512

        private fun mnemonicToSeed(mnemonic: Mnemonic, passphrase: Passphrase): Seed {
            val password = mnemonic.toString().encodeToByteArray()
            val salt = passphrase.toString().encodeToByteArray()

            // https://github.com/horizontalsystems/hd-wallet-kit-android/blob/master/src/main/kotlin/io/horizontalsystems/hdwalletkit/PBKDFSHA512.kt
            // https://github.com/phlay/pspka/blob/master/pspka-pbkdf2-demo/pbkdf2-hmac-sha512.c
            return Seed(PBKDF2.sha512Key(password, salt, radix, kLen))
        }
    }
}

