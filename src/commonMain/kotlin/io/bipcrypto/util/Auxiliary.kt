package io.bipcrypto.util


inline fun reverseShort(value: Short): Short = (
        (value.toInt() shl 8 and 0xFF00) or (value.toInt() shr 8 and 0xFF)).toShort()
inline fun reverseInt(value: Int): Int = (value shl 24 and -0x1000000) or
        (value shl 8 and 0xFF0000) or
        (value shr 8 and 0xFF00) or
        (value shr 24 and 0xFF)
inline fun reverseLong(value: Long): Long = (value shl 56 and -0x1000000_00000000) or
        (value shl 40 and 0xFF0000_00000000) or
        (value shl 24 and 0xFF00_00000000) or
        (value shl 8 and 0xFF_00000000) or
        (value shr 8 and 0xFF000000) or
        (value shr 24 and 0xFF0000) or
        (value shr 40 and 0xFF00) or
        (value shr 56 and 0xFF)

inline fun Short.swapEndian(): Short = reverseShort(this)
inline fun UShort.swapEndian(): UShort = reverseShort(this.toShort()).toUShort()
inline fun Char.swapEndian(): Char = reverseShort(this.code.toShort()).toInt().toChar()
inline fun Int.swapEndian(): Int = reverseInt(this)
inline fun UInt.swapEndian(): UInt = reverseInt(this.toInt()).toUInt()
inline fun Long.swapEndian(): Long = reverseLong(this)
inline fun ULong.swapEndian(): ULong = reverseLong(this.toLong()).toULong()
inline fun Float.swapEndian(): Float = Float.fromBits(reverseInt(this.toBits()))
inline fun Double.swapEndian(): Double = Double.fromBits(reverseLong(this.toBits()))

inline fun ByteArray.readShortAt(offset: Int): Short = (
        (this[offset + 1].toInt() shl 8 and 0xFF00) or (this[offset + 0].toInt() and 0xFF)
        ).toShort()
inline fun ByteArray.readUShortAt(offset: Int): UShort = readShortAt(offset).toUShort()
inline fun ByteArray.readCharAt(offset: Int): Char = readShortAt(offset).toInt().toChar()
inline fun ByteArray.readIntAt(offset: Int): Int = (this[offset + 3].toInt() shl 24 and -0x1000000) or
        (this[offset + 2].toInt() shl 16 and 0xFF0000) or
        (this[offset + 1].toInt() shl 8 and 0xFF00) or
        (this[offset + 0].toInt() and 0xFF)
inline fun ByteArray.readUIntAt(offset: Int): UInt = readIntAt(offset).toUInt()
inline fun ByteArray.readLongAt(offset: Int): Long = (this[offset + 7].toLong() shl 56 and -0x1000000_00000000) or
        (this[offset + 6].toLong() shl 48 and 0xFF0000_00000000) or
        (this[offset + 5].toLong() shl 40 and 0xFF00_00000000) or
        (this[offset + 4].toLong() shl 32 and 0xFF_00000000) or
        (this[offset + 3].toLong() shl 24 and 0xFF000000) or
        (this[offset + 2].toLong() shl 16 and 0xFF0000) or
        (this[offset + 1].toLong() shl 8 and 0xFF00) or
        (this[offset + 0].toLong() and 0xFF)
inline fun ByteArray.readULongAt(offset: Int): ULong = readLongAt(offset).toULong()
inline fun ByteArray.readFloatAt(offset: Int): Float = Float.fromBits(readIntAt(offset))
inline fun ByteArray.readDoubleAt(offset: Int): Double = Double.fromBits(readLongAt(offset))
inline fun ByteArray.writeShortAt(offset: Int, value: Short) {
    this[offset + 1] = (value.toInt() shr 8 and 0xFF).toByte()
    this[offset] = (value.toInt() and 0xFF).toByte()
}
inline fun ByteArray.writeUShortAt(offset: Int, value: UShort) = writeShortAt(offset, value.toShort())
inline fun ByteArray.writeCharAt(offset: Int, value: Char) = writeShortAt(offset, value.code.toShort())
inline fun ByteArray.writeIntAt(offset: Int, value: Int) {
    this[offset + 3] = (value shr 24 and 0xFF).toByte()
    this[offset + 2] = (value shr 16 and 0xFF).toByte()
    this[offset + 1] = (value shr 8 and 0xFF).toByte()
    this[offset] = (value and 0xFF).toByte()
}
inline fun ByteArray.writeUIntAt(offset: Int, value: UInt) = writeIntAt(offset, value.toInt())
inline fun ByteArray.writeLongAt(offset: Int, value: Long) {
    this[offset + 7] = (value shr 56 and 0xFF).toByte()
    this[offset + 6] = (value shr 48 and 0xFF).toByte()
    this[offset + 5] = (value shr 40 and 0xFF).toByte()
    this[offset + 4] = (value shr 32 and 0xFF).toByte()
    this[offset + 3] = (value shr 24 and 0xFF).toByte()
    this[offset + 2] = (value shr 16 and 0xFF).toByte()
    this[offset + 1] = (value shr 8 and 0xFF).toByte()
    this[offset] = (value and 0xFF).toByte()
}
inline fun ByteArray.writeULongAt(offset: Int, value: ULong) = writeLongAt(offset, value.toLong())
inline fun ByteArray.writeFloatAt(offset: Int, value: Float) = writeIntAt(offset, value.toBits())
inline fun ByteArray.writeDoubleAt(offset: Int, value: Double) = writeLongAt(offset, value.toBits())