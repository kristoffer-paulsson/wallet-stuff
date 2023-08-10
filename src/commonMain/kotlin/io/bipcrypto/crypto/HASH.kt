package io.bipcrypto.crypto

object HASH {

    fun sha256(data: ByteArray): Sha256Digest {
        val state = Algorithm.SHA256.factory()
        state.update(data)
        return Sha256Digest(state.final())
    }

    fun sha512(data: ByteArray): Sha512Digest {
        val state = Algorithm.SHA512.factory()
        state.update(data)
        return Sha512Digest(state.final())
    }

    fun sha256(munch: () -> ByteArray): Sha256Digest {
        val state = Algorithm.SHA256.factory()
        var data = munch()
        while (data.isNotEmpty()) {
            state.update(data)
            data = munch()
        }
        return Sha256Digest(state.final())
    }

    fun sha512(munch: () -> ByteArray): Sha512Digest {
        val state = Algorithm.SHA512.factory()
        var data = munch()
        while (data.isNotEmpty()) {
            state.update(data)
            data = munch()
        }
        return Sha512Digest(state.final())
    }
}