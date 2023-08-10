package io.bipcrypto.bip39

enum class Strength(val length: Int, val size: Int, val checksum: Int, val count: Int) {
    DEFAULT(128, 16, 4, 12),
    LOW(160, 20,5, 15),
    MEDIUM(192, 24, 6, 18),
    HIGH(224, 28,7, 21),
    VERY_HIGH(256, 32, 8, 24);

    companion object {
        fun byLength(length: Int) = when(length) {
            DEFAULT.length -> DEFAULT
            LOW.length -> LOW
            MEDIUM.length -> MEDIUM
            HIGH.length -> HIGH
            VERY_HIGH.length -> VERY_HIGH
            else -> error("Invalid length for standard strength.")
        }

        fun bySize(size: Int) = when(size) {
            DEFAULT.size -> DEFAULT
            LOW.size -> LOW
            MEDIUM.size -> MEDIUM
            HIGH.size -> HIGH
            VERY_HIGH.size -> VERY_HIGH
            else -> error("Invalid size for standard strength.")
        }

        fun byCount(count: Int) = when(count) {
            DEFAULT.count -> DEFAULT
            LOW.count -> LOW
            MEDIUM.count -> MEDIUM
            HIGH.count -> HIGH
            VERY_HIGH.count -> VERY_HIGH
            else -> error("Invalid count for standard strength.")
        }
    }
}