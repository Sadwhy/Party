package io.sadwhy.party.utils

import android.os.Build
import android.os.Bundle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable

object Serializer {
    val json = Json {
        ignoreUnknownKeys = true
    }

    inline fun <reified T> String.toData() = json.decodeFromString<T>(this)
    inline fun <reified T> T.toJson() = json.encodeToString(this)

    inline fun <reified T> Bundle.putSerialized(key: String, value: T) {
        putString(key, value.toJson())
    }

    inline fun <reified T> Bundle.getSerialized(key: String): T? {
        return getString(key)?.toData()
    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Serializable> Bundle.getSerial(key: String?) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            getSerializable(key, T::class.java)
        else getSerializable(key) as T

    val Throwable.rootCause: Throwable
        get() = generateSequence(this) { it.cause }.last()
}