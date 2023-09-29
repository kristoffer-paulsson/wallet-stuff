package io.bipcrypto.bip39

import doist.x.normalize.Form
import doist.x.normalize.normalize
import kotlin.jvm.JvmInline

@JvmInline
public value class Passphrase(private val passphrase: String = "") {
    public fun toSalt(): ByteArray = "mnemonic$passphrase".normalize(Form.NFKD).encodeToByteArray()
}