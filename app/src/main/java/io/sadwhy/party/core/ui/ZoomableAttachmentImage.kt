package io.sadwhy.party.core.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import me.saket.telephoto.zoomable.rememberZoomablePeekOverlayState
import me.saket.telephoto.zoomable.zoomablePeekOverlay
import io.sadwhy.party.data.model.Attachment
import okhttp3.HttpUrl

@Composable
fun ZoomableAttachmentImage(
    domain: String,
    attachment: Attachment,
    onLongClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val fullImageUrl = remember(domain, attachment.path, attachment.name) {
        buildFullImageUrl(domain, attachment)
    }

    val thumbnailUrl = remember(domain, attachment.path) {
        buildThumbnailUrl(domain, attachment)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .zoomablePeekOverlay(rememberZoomablePeekOverlayState())
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .crossfade(false)
                .build(),
            contentDescription = "Image Attachment",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    if (onLongClick != null) {
                        detectTapGestures(onLongPress = { onLongClick() })
                    }
                },
            loading = {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(thumbnailUrl)
                        .build(),
                    contentDescription = "Image Attachment",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

private fun buildFullImageUrl(domain: String, attachment: Attachment): String {
    val urlBuilder = HttpUrl.Builder()
        .scheme("https")
        .host("$domain.su")
        .addPathSegment("data${attachment.path}")

    if (!attachment.name.isNullOrBlank()) {
        urlBuilder.addQueryParameter("f", attachment.name)
    }

    return urlBuilder.build().toString()
}

private fun buildThumbnailUrl(domain: String, attachment: Attachment): String {
    return HttpUrl.Builder()
        .scheme("https")
        .host("img.$domain.su")
        .addPathSegment("thumbnail")
        .addPathSegment("data${attachment.path}")
        .build()
        .toString()
}