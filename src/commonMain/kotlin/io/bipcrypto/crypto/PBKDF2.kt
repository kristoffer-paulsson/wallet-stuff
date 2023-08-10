package io.bipcrypto.crypto

object PBKDF2 {
    fun sha256(kLen: Int, c: Int): Pbkdf2Derive = Pbkdf2Derive(Algorithm.SHA256, kLen / 8, c)

    fun sha512(kLen: Int, c: Int): Pbkdf2Derive = Pbkdf2Derive(Algorithm.SHA512, kLen / 8, c)

    fun sha256Key(p: ByteArray, s: ByteArray, c: Int, kLen: Int): ByteArray = sha256(kLen, c).masterKey(p, s)

    fun sha512Key(p: ByteArray, s: ByteArray, c: Int, kLen: Int): ByteArray = sha512(kLen, c).masterKey(p, s)
}