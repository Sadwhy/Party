package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Post(
    val id: String,
    val user: String,
    val service: String,
    val title: String? = null,
    val content: String? = null,
    val embed: JsonElement? = null, // Need to look into this
    val shared_file: Boolean? = null,
    val added: String? = null,
    val published: String? = null,
    val edited: String? = null,
    val file: File? = null,
    val attachments: List<Attachment>? = null,
    val substring: String? = null,
)

@Serializable
data class File(
    val name: String? = null,
    val path: String? = null,
)