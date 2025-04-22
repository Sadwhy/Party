package io.sadwhy.party.media.model

import kotlinx.serialization.Serializable

@Serializable
data class Creator(
    val id: String,
    val name: String,
    val service: String,
    val indexed: Long,
    val updated: Long,
    val favorited: Int
)