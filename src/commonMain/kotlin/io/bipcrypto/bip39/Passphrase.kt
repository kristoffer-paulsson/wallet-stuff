package io.bipcrypto.bip39

import kotlin.jvm.JvmInline

@JvmInline
value class Passphrase(val passphrase: String = "") {
    override fun toString(): String = "mnemonic$passphrase"
}