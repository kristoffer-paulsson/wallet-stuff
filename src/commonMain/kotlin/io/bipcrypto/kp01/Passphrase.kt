package io.bipcrypto.kp01

import org.angproj.crypt.sha.Sha384Hash
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

public class Passphrase internal constructor(
    private val _passphrase: ByteArray,
    base: ByteArray,
    checksum: ByteArray
) {

    init {
        require(base.size >= 8) { "The base of the passphrase must be at least 8 bytes." }
        require(calculateChecksum(base).contentEquals(checksum)) {
            "Passphrase checksum invalid: invalid passphrase, try enter it correctly." }
    }

    public val passphrase: ByteArray
        get() = _passphrase

    public fun toSalt(): ByteArray = "mnemonic".encodeToByteArray() + _passphrase

    @OptIn(ExperimentalEncodingApi::class)
    public override fun toString(): String = Base64.encode(_passphrase)

    private constructor(
        passphrase: ByteArray,
        elements: Pair<ByteArray, ByteArray>
    ): this(passphrase, elements.first, elements.second)

    internal constructor(passphrase: ByteArray): this(passphrase, splitPassphrase(passphrase))

    internal constructor(
        elements: Pair<ByteArray, ByteArray>
    ): this(elements.first + elements.second, elements)

    public companion object {

        public fun generatePassphrase(base: ByteArray): Pair<ByteArray, ByteArray> = Pair(
            base,
            calculateChecksum(base)
        )

        public fun splitPassphrase(passphrase: ByteArray): Pair<ByteArray, ByteArray> = Pair(
            passphrase.sliceArray(0..passphrase.size-2),
            passphrase.sliceArray(passphrase.size-2..passphrase.size)
        )

        public fun calculateChecksum(base: ByteArray): ByteArray {
            val sha = Sha384Hash.create()
            sha.update(base.sliceArray(0..base.size-2))
            return sha.final().copyOfRange(0, 2)
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
public fun passphraseOf(code: String): Passphrase = Passphrase(Base64.decode(code))

public fun newPassphraseFrom(base: ByteArray): Passphrase = Passphrase(Passphrase.generatePassphrase(base))