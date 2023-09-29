package io.bipcrypto.util


public object Base58 {

    private const val ENCODED_ZERO = '1'
    private const val alphabet = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
    private val alphabetIndices by lazy { IntArray(128) { alphabet.indexOf(it.toChar()) } }

    private fun divmod(number: ByteArray, firstDigit: UInt, base: UInt, divisor: UInt): UInt {
        // this is just long division which accounts for the base of the input digits
        var remainder = 0.toUInt()
        for (i in firstDigit until number.size.toUInt()) {
            val digit = number[i.toInt()].toUByte()
            val temp = remainder * base + digit
            number[i.toInt()] = (temp / divisor).toByte()
            remainder = temp % divisor
        }
        return remainder
    }
    public fun encode(rawBytes: ByteArray): String {
        require(rawBytes.size < 256) { "Not recommended to Base58 encode data larger than 256 bytes." }

        val input = rawBytes.copyOf(rawBytes.size) // since we modify it in-place
        if (input.isEmpty()) {
            return ""
        }
        // Count leading zeros.
        var zeros = 0
        while (zeros < input.size && input[zeros].toInt() == 0) {
            ++zeros
        }
        // Convert base-256 digits to base-58 digits (plus conversion to ASCII characters)
        val encoded = CharArray(input.size * 2) // upper bound
        var outputStart = encoded.size
        var inputStart = zeros
        while (inputStart < input.size) {
            encoded[--outputStart] = alphabet[divmod(input, inputStart.toUInt(), 256.toUInt(), 58.toUInt()).toInt()]
            if (input[inputStart].toInt() == 0) {
                ++inputStart // optimization - skip leading zeros
            }
        }
        // Preserve exactly as many leading encoded zeros in output as there were leading zeros in data.
        while (outputStart < encoded.size && encoded[outputStart] == ENCODED_ZERO) {
            ++outputStart
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = ENCODED_ZERO
        }
        // Return encoded string (including encoded leading zeros).
        return encoded.concatToString(outputStart, outputStart + (encoded.size - outputStart))
    }

    public fun decode(encodedBytes: String): ByteArray {
        if (encodedBytes.isEmpty()) {
            return ByteArray(0)
        }
        // Convert the base58-encoded ASCII chars to a base58 byte sequence (base58 digits).
        val input58 = ByteArray(encodedBytes.length)
        for (i in 0 until encodedBytes.length) {
            val c = encodedBytes[i]
            val digit = if (c.code < 128) alphabetIndices[c.code] else -1
            if (digit < 0) {
                throw NumberFormatException("Illegal character $c at position $i")
            }
            input58[i] = digit.toByte()
        }
        // Count leading zeros.
        var zeros = 0
        while (zeros < input58.size && input58[zeros].toInt() == 0) {
            ++zeros
        }
        // Convert base-58 digits to base-256 digits.
        val decoded = ByteArray(encodedBytes.length)
        var outputStart = decoded.size
        var inputStart = zeros
        while (inputStart < input58.size) {
            decoded[--outputStart] = divmod(input58, inputStart.toUInt(), 58.toUInt(), 256.toUInt()).toByte()
            if (input58[inputStart].toInt() == 0) {
                ++inputStart // optimization - skip leading zeros
            }
        }
        // Ignore extra leading zeroes that were added during the calculation.
        while (outputStart < decoded.size && decoded[outputStart].toInt() == 0) {
            ++outputStart
        }
        // Return decoded data (including original number of leading zeros).
        return decoded.copyOfRange(outputStart - zeros, decoded.size)
    }
}