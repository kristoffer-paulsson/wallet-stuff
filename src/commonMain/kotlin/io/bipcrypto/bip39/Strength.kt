package io.bipcrypto.bip39

enum class Strength(val bitLength: Int, val size: Int, val checksum: Int, val wordCount: Int) {
    DEFAULT(128, 16, 4, 12),
    LOW(160, 20, 5, 15),
    MEDIUM(192, 24, 6, 18),
    HIGH(224, 28, 7, 21),
    VERY_HIGH(256, 32, 8, 24);

    companion object {
        val sizes = setOf(DEFAULT.size, LOW.size, MEDIUM.size, HIGH.size, VERY_HIGH.size)
        val counts = setOf(DEFAULT.wordCount, LOW.wordCount, MEDIUM.wordCount, HIGH.wordCount, VERY_HIGH.wordCount)

        fun byLength(length: Int) = when (length) {
            DEFAULT.bitLength -> DEFAULT
            LOW.bitLength -> LOW
            MEDIUM.bitLength -> MEDIUM
            HIGH.bitLength -> HIGH
            VERY_HIGH.bitLength -> VERY_HIGH
            else -> error("Invalid length for standard strength.")
        }

        fun bySize(size: Int) = when (size) {
            DEFAULT.size -> DEFAULT
            LOW.size -> LOW
            MEDIUM.size -> MEDIUM
            HIGH.size -> HIGH
            VERY_HIGH.size -> VERY_HIGH
            else -> error("Invalid size for standard strength.")
        }

        fun byCount(count: Int) = when (count) {
            DEFAULT.wordCount -> DEFAULT
            LOW.wordCount -> LOW
            MEDIUM.wordCount -> MEDIUM
            HIGH.wordCount -> HIGH
            VERY_HIGH.wordCount -> VERY_HIGH
            else -> error("Invalid count for standard strength.")
        }
    }
}