package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val post: Post
)
