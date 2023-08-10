package vect

import io.bipcrypto.util.BinHex
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

interface Vector {

    val testVectors: String

    fun msgIter(file: String, process: (msg: ByteArray, md: String) -> Unit) {
        val data = file.split("]\n\n")[1]
        data.split("\n\n").forEach { entry ->
            val rows = entry.split("\n")
            val size = rows[0].substring(6).trim().toInt() / 8
            val message = BinHex.decodeToBin(rows[1].substring(6).trim()).copyOfRange(0, size)
            val digest = rows[2].substring(5).trim().lowercase()
            process(message, digest)
        }
    }

    fun msgIter(file: String, process: (msg: ByteArray, key: ByteArray, md: String) -> Unit) {
        val data = file.split("]\n\n")[1]
        data.split("\n\n").forEach { entry ->
            val rows = entry.split("\n")
            val count = rows[0].substring(8).trim().toInt()
            val klen = rows[1].substring(7).trim().toInt()
            val tlen = rows[2].substring(7).trim().toInt()
            val key = BinHex.decodeToBin(rows[3].substring(6).trim()).copyOfRange(0, klen)
            val message = BinHex.decodeToBin(rows[4].substring(6).trim())
            val mac = rows[5].substring(6).trim().lowercase()
            process(message, key, mac)
        }
    }

    fun bip39JsonIter(file: String, process: (lang: String, entropy: ByteArray, sentence: String, p1: String, p2: String) -> Unit) {
        val data = Json.decodeFromString<JsonObject>(file)
        data.forEach {
            val lang = it.key
            it.value.jsonArray.forEach { el ->
                val items = mutableListOf<String>()
                el.jsonArray.forEach { item -> items.add(item.jsonPrimitive.content) }
                process(lang, BinHex.decodeToBin(items[0]), items[1], items[2], items[3])
            }
        }
    }
}