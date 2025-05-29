package io.sadwhy.party.ui.media

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import io.sadwhy.party.data.model.Attachment
import me.saket.telephoto.zoomable.ZoomableImage
import me.saket.telephoto.zoomable.rememberZoomableImageState
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.HttpUrl

@Composable
fun ZoomableAttachmentImage(
    domain: String,
    a: Attachment,
    onDoubleClick: () -> Unit
) {
    val imageUrl = run {
        val baseUrl = "https://$domain.su".toHttpUrlOrNull()
        baseUrl?.newBuilder()
            ?.addPathSegment("data${a.path}")
            ?.apply {
                if (!a.name.isNullOrEmpty()) {
                    addQueryParameter("f", a.name)
                }
            }
            ?.build()
            .toString()
    }

    val placeholderUrl = run {
        val baseUrl = "https://img.$domain".toHttpUrlOrNull()
        baseUrl?.newBuilder()
            ?.addPathSegment("thumbnail")
            ?.addPathSegment("data${a.path}")
            ?.build()
            .toString()
    }

    val placeholderPainter: Painter = rememberAsyncImagePainter(placeholderUrl)

    ZoomableImage(
        state = rememberZoomableImageState(),
        onDoubleTap = { onDoubleClick() }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
            placeholder = placeholderPainter
        )
    }
}