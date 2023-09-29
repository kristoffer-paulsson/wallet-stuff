package io.bipcrypto.util

internal abstract class AbstractEndianAware(override val littleEndian: Boolean = true) : EndianAware {
    protected fun <E> be(v: E, e: (v: E) -> E): E = when (littleEndian) {
        true -> v
        else -> e(v)
    }

    protected fun <E> le(v: E, e: (v: E) -> E): E = when (littleEndian) {
        true -> e(v)
        else -> v
    }
}