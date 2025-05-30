package io.sadwhy.party.ui.media

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
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun ZoomableAttachmentImage(
    domain: String,
    a: Attachment,
    onLongClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var imageLoaded by remember { mutableStateOf(false) }

    val fullImageUrl = remember(domain, a.path, a.name) {  
        buildFullImageUrl(domain, a)  
    }  

    val thumbnailUrl = remember(domain, a.path) {  
        buildThumbnailUrl(domain, a)  
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!imageLoaded) {
                    Modifier.aspectRatio(16f / 9f)
                } else {
                    Modifier.wrapContentHeight()
                }
            )
            .zoomablePeekOverlay(rememberZoomablePeekOverlayState())
    ) {
        SubcomposeAsyncImage(  
            model = ImageRequest.Builder(context)  
                .data(fullImageUrl)  
                .crossfade(false)  
                .build(),  
            contentDescription = null,  
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
                    contentDescription = null,  
                    contentScale = ContentScale.FillWidth,  
                    modifier = Modifier.fillMaxWidth(),
                    onSuccess = {
                        imageLoaded = true
                    }
                )  
            }  
        )
    }
}

private fun buildFullImageUrl(domain: String, attachment: Attachment): String {
    return "https://$domain.su".toHttpUrlOrNull()
        ?.newBuilder()
        ?.addPathSegment("data${attachment.path}")
        ?.apply {
            attachment.name?.takeIf { it.isNotEmpty() }?.let {
                addQueryParameter("f", it)
            }
        }
        ?.build()
        ?.toString()
        .orEmpty()
}

private fun buildThumbnailUrl(domain: String, attachment: Attachment): String {
    return "https://img.$domain.su".toHttpUrlOrNull()
        ?.newBuilder()
        ?.addPathSegment("thumbnail")
        ?.addPathSegment("data${attachment.path}")
        ?.build()
        ?.toString()
        .orEmpty()
}