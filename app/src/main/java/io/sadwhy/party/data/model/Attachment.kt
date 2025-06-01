package io.sadwhy.party.data.model

import android.webkit.MimeTypeMap
import kotlinx.serialization.Serializable

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