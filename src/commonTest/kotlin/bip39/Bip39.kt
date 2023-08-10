package bip39

import io.bipcrypto.bip39.*
import vect.BIP39_Trezor
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails

class Bip39Test {

    val language = mapOf(
        "english" to Language.ENGLISH,
        "chinese_simplified" to Language.CHINESE_SIMPLIFIED,
        "chinese_traditional" to Language.CHINESE_TRADITIONAL,
        "czech" to Language.CZECH,
        "french" to Language.FRENCH,
        "italian" to Language.ITALIAN,
        "japanese" to Language.JAPANESE,
        "korean" to Language.KOREAN,
        "portuguese" to Language.PORTUGUESE,
        "spanish" to Language.SPANISH,
    )

    @Test
    fun entropyToMnemonic() {
        BIP39_Trezor.bip39JsonIter(BIP39_Trezor.testVectors) { l, e, m, p1, p2 ->
            val lang = language.get(l) ?: Language.ENGLISH
            if(lang == Language.JAPANESE) // Japanese test vectors must be normalized by NFKD to work properly
                return@bip39JsonIter

            val bip39 = Bip39(
                entropy = Entropy(e),
                language = lang,
                passphrase = Passphrase("TREZOR"),
                strength = Strength.bySize(e.size)
            )

            assertContentEquals(bip39.mnemonic.toString().encodeToByteArray(), m.encodeToByteArray())
            assertEquals(bip39.seed.toHex().lowercase(), p1.lowercase())
        }
    }

    @Test
    fun MnemonicToEntropy() {
        BIP39_Trezor.bip39JsonIter(BIP39_Trezor.testVectors) { l, e, m, p1, p2 ->
            val lang = language.get(l) ?: Language.ENGLISH
            if(lang == Language.JAPANESE) // Japanese test vectors must be normalized by NFKD to work properly
                return@bip39JsonIter

            val bip39 = Bip39(
                mnemonic = Mnemonic(m.split(' ')),
                language = language.get(l.trim()) ?: Language.ENGLISH,
                passphrase = Passphrase("TREZOR"),
                strength = Strength.bySize(e.size)
            )

            assertContentEquals(bip39.entropy.entropy, e)
            assertEquals(bip39.seed.toHex().lowercase(), p1.lowercase())
        }
    }

    @Test
    fun entropyMnemonicFuzzing() {
        val fuzzVectors = mutableListOf<ByteArray>()
        (0..19).forEach { _ -> fuzzVectors.add(Random.nextBytes(ByteArray(Strength.DEFAULT.size))) }
        (0..19).forEach { _ -> fuzzVectors.add(Random.nextBytes(ByteArray(Strength.LOW.size))) }
        (0..19).forEach { _ -> fuzzVectors.add(Random.nextBytes(ByteArray(Strength.MEDIUM.size))) }
        (0..19).forEach { _ -> fuzzVectors.add(Random.nextBytes(ByteArray(Strength.HIGH.size))) }
        (0..19).forEach { _ -> fuzzVectors.add(Random.nextBytes(ByteArray(Strength.VERY_HIGH.size))) }
        fuzzVectors.shuffle()

        language.forEach { l ->
            fuzzVectors.forEach { fv ->
                val e = Entropy(fv)
                val bip39A = Bip39(
                    entropy = e,
                    language = l.value,
                    strength = Strength.bySize(fv.size)
                )

                val m = bip39A.mnemonic

                val bip39B = Bip39(
                    mnemonic = m,
                    language = l.value,
                    strength = Strength.byCount(m.words.size)
                )

                assertContentEquals(e.entropy, bip39B.entropy.entropy)

                // Exchanging the last word in the mnemonic to achieve an invalid checksum.
                val vl = WordList.getDictionary(l.value)
                val m2 = Mnemonic(
                    m.words.subList(0, m.words.size - 1) + vl.wordAt(
                        (vl.wordOf(m.words.last()))+1.mod(2048))
                )

                assertFails {
                    val bip39C = Bip39(
                        mnemonic = m2,
                        language = l.value,
                        strength = Strength.byCount(m2.words.size)
                    )
                }
            }
        }
    }
}