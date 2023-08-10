package io.bipcrypto.crypto

import kotlin.jvm.JvmInline


@JvmInline
value class Sha512Digest internal constructor(private val _raw: ByteArray): HashDigest {
    init {
        require(_raw.size == Sha512Engine.OUT_SIZE)
    }

    override val raw: ByteArray
        get() = _raw

}