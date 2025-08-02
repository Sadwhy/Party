package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val name: String? = null,
    val path: String? = null,
)