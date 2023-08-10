package io.bipcrypto.crypto

/**
 * https://csrc.nist.gov/files/pubs/fips/198-1/final/docs/fips-198-1_final.pdf
 */
class HmacEngine(prepared: HmacKey): Engine{

    constructor(key: ByteArray, algo: Algorithm): this(prepareHmacKey(key, algo))

    private val inner = prepared.algo.factory()
    private val outer = prepared.algo.factory()

    init {
        inner.update(prepared.iPadKey)
        outer.update(prepared.oPadKey)
    }

    override fun update(messagePart: ByteArray) = inner.update(messagePart)

    override fun final(): ByteArray {
        outer.update(inner.final())
        return outer.final()
    }

    companion object {
        private fun determineKey(key: ByteArray, algo: Algorithm): ByteArray = when {
            key.size == algo.blkSize -> key
            key.size > algo.blkSize -> {
                val k0 = algo.factory()
                k0.update(key)
                k0.final().copyOf(algo.blkSize)
            }
            key.size < algo.blkSize -> key.copyOf(algo.blkSize)
            else -> error("Could not determine key")
        }

        private fun pad(filler: Byte, algo: Algorithm): ByteArray {
            val c = ByteArray(algo.blkSize)
            c.fill(filler)
            return c
        }

        private fun padKey(k0: ByteArray, pad: ByteArray, algo: Algorithm): ByteArray {
            val xPad = ByteArray(algo.blkSize)
            xPad.indices.forEachIndexed { idx, _ -> xPad[idx] = (k0[idx].toInt() xor pad[idx].toInt()).toByte() }
            return xPad
        }

        fun prepareHmacKey(key: ByteArray, algo: Algorithm): HmacKey {
            val key0 = determineKey(key, algo)
            return HmacKey(Triple(
                padKey(key0, pad(0x36, algo), algo),
                padKey(key0, pad(0x5c, algo), algo),
                algo
            ))
        }
    }
}