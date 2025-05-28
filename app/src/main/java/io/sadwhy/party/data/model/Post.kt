package io.sadwhy.party.data.model

import android.webkit.MimeTypeMap
import kotlinx.serialization.Serializable

@Serializable
data class Recent(
    val count: Long,
    val true_count: Long,
    val posts: List<Post>? = null,
)

@Serializable
data class Post(
    val id: String,
    val user: String,
    val service: String,
    val title: String? = null,
    val content: String? = null,
    val embed: String? = null, // Need to look into this
    val shared_file: Boolean? = null,
    val added: String? = null,
    val published: String? = null,
    val edited: String? = null,
    val file: Attachment? = null,
    val attachments: List<Attachment>? = null,
    val substring: String? = null,
)

@Serializable
data class Attachment(
    val name: String? = null,
    val path: String? = null,
) {
    val mediaType: MediaType?
        get() {
            if (path.isNullOrBlank()) return null

            val extension = MimeTypeMap.getFileExtensionFromUrl(path)?.lowercase()
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
data class PostResponse(
    val post: Post,
    val attachments: List<Attachment> = emptyList(),
    val previews: List<Preview> = emptyList(),
    val videos: List<Video> = emptyList(),
    val props: Props? = null
)

@Serializable
enum class MediaType {
    IMAGE,
    VIDEO,
    FILE
}