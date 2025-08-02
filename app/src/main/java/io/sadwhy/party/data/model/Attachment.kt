package io.sadwhy.party.data.model

import android.webkit.MimeTypeMap
import kotlinx.serialization.Serializable

/**
 * Represents a media attachment associated with a post.
 *
 * @property name Optional name of the file.
 * @property path Server-relative path to the file. Does not include domain.
 *
 * The media type is determined based on the file extension and is used internally
 * to decide whether the UI should display it as an image, video, or generic downloadable file.
 */
@Serializable
data class Attachment(
    val name: String? = null,
    val path: String,
) {
    /**
     * Determines the type of media based on the MIME type derived from the file extension.
     * 
     * - Returns [MediaType.IMAGE] if the file is an image.
     * - Returns [MediaType.VIDEO] if the file is a video.
     * - Returns [MediaType.FILE] for all other types.
     */
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

/**
 * Internal media type categorization for attachments.
 *
 * This enum is **not** part of the serialized JSON.
 * It is used internally to determine how an attachment should be rendered or interacted with.
 */
@Serializable
enum class MediaType {
    IMAGE,
    VIDEO,
    FILE
}