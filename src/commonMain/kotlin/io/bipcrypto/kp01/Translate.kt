package io.bipcrypto.kp01

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

public object Translate {

    @OptIn(ExperimentalEncodingApi::class)
    public fun encode(data: ByteArray): String {
        val text = Base64.encode(data)
        return text.replace('0', '$').replace(
            'I', '!').replace('O', '#').replace(
            '/', '*').replace('+', '%').replace('l', '&')
    }

    @OptIn(ExperimentalEncodingApi::class)
    public fun decode(text: String): ByteArray {
        val temp = text.replace('$', '0').replace(
            '!', 'I').replace('#', 'O').replace(
            '*', '/').replace('%', '+').replace('&', 'l')
        return Base64.decode(temp)
    }
}