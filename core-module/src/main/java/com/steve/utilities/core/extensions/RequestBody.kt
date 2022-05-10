package com.steve.utilities.core.extensions

import com.google.gson.JsonObject
import okhttp3.RequestBody
import java.io.IOException
import java.security.MessageDigest

fun String.hash256(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

fun RequestBody.bodyToString(): String {
    return try {
        val buffer = okio.Buffer()
        if (this != null) this.writeTo(buffer) else return ""
        buffer.readUtf8()
    } catch (e: IOException) {
        "did not work"
    }
}

fun JsonObject.mergeJsonObject(json2Obj: JsonObject): JsonObject {
    val entrySet1 = this.entrySet()
    for (entry in entrySet1) {
        val key1 = entry.key
        if (json2Obj[key1] != null) {
            val tempEle2 = json2Obj[key1]
            val tempEle1 = entry.value
            if (tempEle2.isJsonObject && tempEle1.isJsonObject) {
                val mergedObj = tempEle1.asJsonObject.mergeJsonObject(
                    tempEle2.asJsonObject
                )
                entry.setValue(mergedObj)
            }
        }
    }
    val entrySet2 = json2Obj.entrySet()
    for ((key2, value) in entrySet2) {
        if (this[key2] == null) {
            this.add(key2, value)
        }
    }
    return this
}