package io.bipcrypto.kp01

import org.angproj.crypt.sha.Sha384Hash
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

public class Passphrase internal constructor(
    private val base: ByteArray,
    private val checksum: ByteArray
) {

    init {
        require(base.size >= 8) { "The base of the passphrase must be at least 8 bytes." }
        require(calculateChecksum(base).contentEquals(checksum)) {
            "Passphrase checksum invalid: invalid passphrase, try enter it correctly." }
    }

    public val passphrase: ByteArray
        get() = base + checksum

    public fun toSalt(): ByteArray = "mnemonic".encodeToByteArray() + passphrase

    @OptIn(ExperimentalEncodingApi::class)
    public override fun toString(): String = Base64.encode(passphrase)

    private constructor(elements: Pair<ByteArray, ByteArray>): this(elements.first, elements.second)

    internal constructor(passphrase: ByteArray): this(splitPassphrase(passphrase))

    @OptIn(ExperimentalEncodingApi::class)
    public constructor(passphrase: String): this(Base64.decode(passphrase))

    public companion object {

        private fun generatePassphrase(base: ByteArray): Pair<ByteArray, ByteArray> = Pair(
            base,
            calculateChecksum(base)
        )

        private fun splitPassphrase(passphrase: ByteArray): Pair<ByteArray, ByteArray> = Pair(
            passphrase.sliceArray(0 until passphrase.size-2),
            passphrase.sliceArray(passphrase.size-2 until passphrase.size)
        )

        private fun calculateChecksum(base: ByteArray): ByteArray {
            val sha = Sha384Hash.create()
            sha.update(base)
            return sha.final().copyOfRange(0, 2)
        }
        public fun newPassphraseFrom(base: ByteArray): Passphrase = Passphrase(generatePassphrase(base))
    }
}
