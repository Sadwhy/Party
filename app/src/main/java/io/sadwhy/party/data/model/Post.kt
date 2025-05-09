package io.sadwhy.party.data.model

import android.webkit.MimeTypeMap
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String? = null,
    val user: String? = null,
    val service: String? = null,
    val title: String? = null,
    val content: String? = null,
    val embed: String? = null, // Need to look into this
    val shared_file: Boolean? = null,
    val added: String? = null,
    val published: String? = null,
    val edited: String? = null,
    val file: Attachment? = null,
    val attachments: List<Attachment>? = null,
    val username: String? = null,
    val description: String? = null,
)

@Serializable
data class Attachment(
    val name: String? = null,
    val path: String? = null,
) {
    val mediaType: MediaType
        get() {
            val extension = path?.let { MimeTypeMap.getFileExtensionFromUrl(it)?.lowercase() }
            val mime = extension?.let {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(it)
            }

            return when {
                mime == null -> MediaType.FILE
                mime.startsWith("video/") -> MediaType.VIDEO
                mime.startsWith("image/") -> MediaType.IMAGE
                else -> MediaType.FILE
            }
        }
}

@Serializable
enum class MediaType {
    IMAGE,
    VIDEO,
    FILE
}