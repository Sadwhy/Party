package io.sadwhy.party.data.model

import android.webkit.MimeTypeMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Recent(
    val count: Long,
    val true_count: Long,
    val posts: List<Post>? = null,
)

@Serializable
data class PostResponse(
    val post: Post
)

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

@Serializable
data class Attachment(
    val name: String? = null,
    val path: String,
) {
    val mediaType: MediaType
        get() {
            val extension = MimeTypeMap.getFileExtensionFromUrl(path).lowercase()
            val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

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