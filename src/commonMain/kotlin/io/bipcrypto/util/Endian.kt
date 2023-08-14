package io.bipcrypto.util

enum class Endian(val order: Int) {
    UNKNOWN(0),
    LITTLE(1),
    BIG(2),
    NET(BIG.order)
}