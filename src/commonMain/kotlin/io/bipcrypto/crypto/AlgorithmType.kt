package io.bipcrypto.crypto

interface AlgorithmType {
    val TYPE: Algorithm
    val BLK_LEN: Int
    val INT_LEN: Int
    val OUT_SIZE: Int
}