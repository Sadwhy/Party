package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PostRecent(
    val count: Long,
    val true_count: Long,
    val posts: List<Post>? = null,
)
