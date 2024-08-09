package io.bipcrypto.bip32

import org.angproj.aux.num.BigInt
import org.angproj.crypt.hmac.KeyHashedMac
import org.angproj.crypt.sha.Sha512Hash
import org.angproj.crypt.sec.Secp256Koblitz1

public class MasterKey(public val seed: MasterSeed) {

    private val l: ByteArray
    private val r: ByteArray

    public val secretKey: ByteArray
        get() = l.copyOf()

    public val chainCode: ByteArray
        get() = r.copyOf()

    init {
        val hmac = KeyHashedMac.create(HMAC_KEY, Sha512Hash)
        hmac.update(seed.seed)
        val digest = hmac.final()

        l = digest.copyOfRange(0, 32)
        r = digest.copyOfRange(32, 64)

        val privKey = Bip32.parse256(l)

        check(privKey.compareTo(BigInt.zero).isGreater()) {
            "Generated secret key must be positive." }
        check(privKey.compareTo(Secp256Koblitz1.domainParameters.n.value).isLesser()) {
            "Generated secret key must be lesser than N." }
    }

    public companion object {
        public val HMAC_KEY: ByteArray = "Bitcoin seed".encodeToByteArray()
    }
}