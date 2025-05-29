package io.sadwhy.party.ui.media

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.target.Target
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import coil3.compose.AsyncImage
import io.sadwhy.party.data.model.Attachment
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun ZoomableAttachmentImage(
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
        val base = "https://img.$domain.su".toHttpUrlOrNull()
        base?.newBuilder()
            ?.addPathSegment("thumbnail")
            ?.addPathSegment("data${a.path}")
            ?.build()
            ?.toString() ?: ""
    }

    var showFullImage by remember { mutableStateOf(false) }
    var fullImageLoaded by remember { mutableStateOf(false) }
    var imageLoadError by remember { mutableStateOf<String?>(null) }

    // Start loading full image in background
    LaunchedEffect(fullImageUrl) {
        if (fullImageUrl.isNotEmpty()) {
            val request = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .target(
                    onSuccess = { 
                        fullImageLoaded = true
                        showFullImage = true 
                        imageLoadError = null
                    },
                    onError = { error ->
                        imageLoadError = "Full image load failed"
                    }
                )
                .build()
            imageLoader.enqueue(request)
        }
    }

    val currentUrl = if (showFullImage) fullImageUrl else thumbnailUrl
    val shouldCrossfade = showFullImage && !fullImageLoaded

    Column {
        // Debug information
        Text(
            text = "DEBUG IMAGE:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Domain: $domain",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Path: ${a.path}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Name: ${a.name}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Full URL: $fullImageUrl",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Thumbnail URL: $thumbnailUrl",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = "Current URL: $currentUrl",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = "Show Full: $showFullImage, Loaded: $fullImageLoaded",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
        
        imageLoadError?.let { error ->
            Text(
                text = "ERROR: $error",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        // Image container with visible border for debugging
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(2.dp)
        ) {
            if (currentUrl.isNotEmpty()) {
                // Try regular AsyncImage first to test if it's a ZoomableAsyncImage issue
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(currentUrl)
                        .apply {
                            if (shouldCrossfade) {
                                crossfade(true)
                            }
                        }
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
                
                /* Commented out ZoomableAsyncImage for testing
                ZoomableAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(currentUrl)
                        .apply {
                            if (shouldCrossfade) {
                                crossfade(true)
                            }
                        }
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    onLongClick = onLongClick?.let { callback ->
                        { _: androidx.compose.ui.geometry.Offset -> callback() }
                    }
                )
                */
            } else {
                Text(
                    text = "No URL generated",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}