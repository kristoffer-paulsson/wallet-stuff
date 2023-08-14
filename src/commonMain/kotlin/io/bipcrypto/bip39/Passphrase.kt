package io.bipcrypto.bip39

import doist.x.normalize.Form
import doist.x.normalize.normalize
import kotlin.jvm.JvmInline

@JvmInline
value class Passphrase(val passphrase: String = "") {
    fun toSalt(): ByteArray = "mnemonic$passphrase".normalize(Form.NFKD).encodeToByteArray()
}