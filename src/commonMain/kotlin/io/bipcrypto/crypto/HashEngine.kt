package io.bipcrypto.crypto

interface HashEngine: Engine {
    val type: Algorithm
}