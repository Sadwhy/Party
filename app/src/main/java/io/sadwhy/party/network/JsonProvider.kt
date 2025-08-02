package io.sadwhy.party.network

import kotlinx.serialization.json.Json

object JsonProvider {
    val json: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }
}