package io.bipcrypto.crypto

enum class Algorithm(val blkSize: Int, val intSize: Int, val hashSize: Int, val factory: () -> HashEngine) {
    SHA256(64, 4, 32, { Sha256Engine() }),
    SHA512(128, 8, 64, { Sha512Engine() })
}