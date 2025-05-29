package io.sadwhy.party.ui.media

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import io.sadwhy.party.data.model.Attachment
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Composable
fun ZoomableAttachmentImage(
    domain: String,
    a: Attachment,
    onLongClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

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

    Column {
        Text("DEBUG IMAGE:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text("Domain: $domain", style = MaterialTheme.typography.labelSmall)
        Text("Path: ${a.path}", style = MaterialTheme.typography.labelSmall)
        Text("Name: ${a.name}", style = MaterialTheme.typography.labelSmall)
        Text("Full URL: $fullImageUrl", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
        Text("Thumbnail URL: $thumbnailUrl", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)

        if (fullImageUrl.isNotEmpty()) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(fullImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(onLongClick) {
                        if (onLongClick != null) {
                            detectTapGestures(onLongPress = { onLongClick() })
                        }
                    },
                loading = {
                    // Show the thumbnail image while loading full image
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(thumbnailUrl)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
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