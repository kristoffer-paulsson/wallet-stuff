# Critique of BIP-0039

1. The key derivation function PBKDF2 is recommended by NIST BUT has no test vectors from that place.
2. A set of test vectors for PBKDF2 can be obtained from rfc6070, however they only work together with SHA-1, which is obsolete for secure hashing.
3. The mnemonic sentence is not specified exactly how to be generated. Never says with or without spaces or which space character to use ASCII or JP for example.
4. The test vectors officially provided for BIP-0039 doesn't cover 15 and 21 word mnemonics only 12, 18 and 24 even if 15 and 21 is part of the specification.
5. The BIP-0039 standard requires the mnemonic sentence and the passphrase to be normalized in NFKD, the IETF recommends in RFC5198 NFC as a standard for interchange.
6. The blank space if it should use is a guesswork, especially told through the official test vectors that JP sentences should use \u3000 but may still be wrong by using ASCII white space, however the implementation is supposed to correct that, implicitly informed by the TREZOR test vectors.

Some developers that implements of BIP-0039 doesn't seem to struggle far enough to find all possiblilites and takes shortcuts. I on the other hand choosed to even implement the hash/mac/kdf algorithms myself despite all research I had to do and struggle to go through!

# Critique of BIP-0032

1. The master key generation requires that the secp256k1 variable N be an unsigned integer, this is however not mentioned in the specification. But is visible in some crypto libraries that encoding the N curve from hexadecimal requires a hack when loaded into i big integer a'la Java.