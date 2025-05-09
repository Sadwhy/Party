package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Creator(
    val id: String,
    val public_id: String? = null,
    val name: String,
    val service: String,
    val indexed: String,
    val updated: String,
    val favorited: Int? = null,
) {
    val indexedLong: Long?
        get() = indexed.toLongOrNull()

    val updatedLong: Long?
        get() = updated.toLongOrNull()
}