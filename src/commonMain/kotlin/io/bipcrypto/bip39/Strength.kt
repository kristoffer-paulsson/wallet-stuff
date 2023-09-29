package io.bipcrypto.bip39

public enum class Strength(
    public val bitLength: Int,
    public val size: Int,
    public val checksum: Int,
    public val wordCount: Int
) {
    DEFAULT(128, 16, 4, 12),
    LOW(160, 20, 5, 15),
    MEDIUM(192, 24, 6, 18),
    HIGH(224, 28, 7, 21),
    VERY_HIGH(256, 32, 8, 24);

    public companion object {
        internal val sizes: Set<Int> = setOf(DEFAULT.size, LOW.size, MEDIUM.size, HIGH.size, VERY_HIGH.size)
        internal val counts: Set<Int> = setOf(DEFAULT.wordCount, LOW.wordCount, MEDIUM.wordCount, HIGH.wordCount, VERY_HIGH.wordCount)

        public fun byLength(length: Int): Strength = when (length) {
            DEFAULT.bitLength -> DEFAULT
            LOW.bitLength -> LOW
            MEDIUM.bitLength -> MEDIUM
            HIGH.bitLength -> HIGH
            VERY_HIGH.bitLength -> VERY_HIGH
            else -> error("Invalid length for standard strength.")
        }

        public fun bySize(size: Int): Strength = when (size) {
            DEFAULT.size -> DEFAULT
            LOW.size -> LOW
            MEDIUM.size -> MEDIUM
            HIGH.size -> HIGH
            VERY_HIGH.size -> VERY_HIGH
            else -> error("Invalid size for standard strength.")
        }

        public fun byCount(count: Int): Strength = when (count) {
            DEFAULT.wordCount -> DEFAULT
            LOW.wordCount -> LOW
            MEDIUM.wordCount -> MEDIUM
            HIGH.wordCount -> HIGH
            VERY_HIGH.wordCount -> VERY_HIGH
            else -> error("Invalid count for standard strength.")
        }
    }
}