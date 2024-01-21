package io.bipcrypto.kp01

public enum class Language(public val iso: String) {
    ENGLISH("en"); // ISO 639 Set 1

    public fun toByteArray(): ByteArray = iso.encodeToByteArray()
}