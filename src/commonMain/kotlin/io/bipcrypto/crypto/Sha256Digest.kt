package io.bipcrypto.crypto

import kotlin.jvm.JvmInline


@JvmInline
value class Sha256Digest internal constructor(private val _raw: ByteArray): HashDigest {
    init {
        require(_raw.size == Sha256Engine.OUT_SIZE)
    }

    override val raw: ByteArray
        get() = _raw

}