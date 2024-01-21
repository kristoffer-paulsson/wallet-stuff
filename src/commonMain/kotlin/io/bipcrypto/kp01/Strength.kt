package io.bipcrypto.kp01

public enum class Strength(
    public val bitLength: Int,
    public val size: Int,
    public val checksum: Int,
    public val wordCount: Int
) {
    DEFAULT(128, 16, 4, 12);
}