package io.bipcrypto.crypto

import io.bipcrypto.util.BinHex

interface HashDigest {
    val raw: ByteArray

    fun toHex(): String = BinHex.encodeToHex(raw)
}