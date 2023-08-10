package io.bipcrypto.crypto

object HMAC {
    fun sha256Key(key: ByteArray): HmacKey = HmacEngine.prepareHmacKey(key, Algorithm.SHA256)

    fun sha512Key(key: ByteArray): HmacKey = HmacEngine.prepareHmacKey(key, Algorithm.SHA512)

    fun sha256(key: ByteArray, msg: ByteArray): Sha256Digest {
        val raw = HmacEngine(key, Algorithm.SHA256)
        raw.update(msg)
        return Sha256Digest(raw.final())
    }

    fun sha512(key: ByteArray, msg: ByteArray): Sha512Digest {
        val raw = HmacEngine(key, Algorithm.SHA512)
        raw.update(msg)
        return Sha512Digest(raw.final())
    }

    fun sha256(hmacKey: HmacKey, munch: () -> ByteArray): Sha256Digest {
        check(hmacKey.algo == Algorithm.SHA256)
        val state = HmacEngine(hmacKey)
        var data = munch()
        while (data.isNotEmpty()) {
            state.update(data)
            data = munch()
        }
        return Sha256Digest(state.final())
    }

    fun sha512(hmacKey: HmacKey, munch: () -> ByteArray): Sha512Digest {
        check(hmacKey.algo == Algorithm.SHA512)
        val state = HmacEngine(hmacKey)
        var data = munch()
        while (data.isNotEmpty()) {
            state.update(data)
            data = munch()
        }
        return Sha512Digest(state.final())
    }
}