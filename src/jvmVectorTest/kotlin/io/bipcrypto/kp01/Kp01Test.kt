package io.bipcrypto.kp01

import kotlin.random.Random
import kotlin.test.Test

class Kp01Test {
    @Test
    fun generatePassphrase() {
        repeat(100) {
            val e = Entropy(Random.nextBytes(16)).toKeys().toMnemonic(Language.ENGLISH)
            val p = Passphrase.newPassphraseFrom(Random.nextBytes(Random.nextInt().mod(3) + 8))
            val kp = Kp01.fromSentence(e.toString(), p.toString())
            println(
                "Mnemonic: ${e}\n" +
                "Passphrase: ${p}\n" +
                "Language: ${kp.mnemonic.language.iso}\n" +
                "Seed (hex): ${kp.seed.toHex()}\n"
            )
        }
    }
}