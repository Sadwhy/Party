package io.sadwhy.party.ui.media

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.target.Target
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import io.sadwhy.party.data.model.Attachment
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun ZoomableAttachmentImageProgressive(
    domain: String,
    a: Attachment,
    onLongClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val imageLoader = remember { ImageLoader(context) }
    
    val fullImageUrl = remember(domain, a.path, a.name) {
        val base = "https://$domain.su".toHttpUrlOrNull()
        base?.newBuilder()
            ?.addPathSegment("data${a.path}")
            ?.apply {
                if (!a.name.isNullOrEmpty()) {
                    addQueryParameter("f", a.name)
                }
            }
            ?.build()
            ?.toString() ?: ""
    }

    val thumbnailUrl = remember(domain, a.path) {
        val base = "https://img.$domain".toHttpUrlOrNull()
        base?.newBuilder()
            ?.addPathSegment("thumbnail")
            ?.addPathSegment("data${a.path}")
            ?.build()
            ?.toString() ?: ""
    }

    var showFullImage by remember { mutableStateOf(false) }
    var fullImageLoaded by remember { mutableStateOf(false) }

    // Start loading full image in background
    LaunchedEffect(fullImageUrl) {
        if (fullImageUrl.isNotEmpty()) {
            val request = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .target(
                    onSuccess = { 
                        fullImageLoaded = true
                        showFullImage = true 
                    }
                )
                .build()
            imageLoader.enqueue(request)
        }
    }

    val currentUrl = if (showFullImage) fullImageUrl else thumbnailUrl
    val crossfade = showFullImage && !fullImageLoaded

    ZoomableAsyncImage(
        model = ImageRequest.Builder(context)
            .data(currentUrl)
            .crossfade(crossfade)
            .build(),
        contentDescription = null,
        modifier = Modifier,
        contentScale = ContentScale.FillWidth,
        onLongClick = onLongClick
    )
}