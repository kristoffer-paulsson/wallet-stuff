package io.bipcrypto.kp01

import org.angproj.aux.sec.SecureEntropy
import org.angproj.aux.sec.SecureRandom
import kotlin.test.Test

class Kp01Test {
    @Test
    fun generatePassphrase() {
        repeat(100) {
            val e = Entropy(SecureEntropy.read(32).sliceArray(0 until 16)).toKeys().toMnemonic(Language.ENGLISH)
            val p = Passphrase.newPassphraseFrom(SecureRandom.read(10))
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