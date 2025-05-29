package io.sadwhy.party.ui.media

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import io.sadwhy.party.data.model.Attachment
import me.saket.telephoto.zoomable.coil3.ZoomableAsyncImage
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull


@Composable
fun ZoomableAttachmentImage(
    domain: String,
    a: Attachment,
    onLongClick: () -> Unit
) {
    val context = LocalContext.current

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

    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .placeholderMemoryCacheKey(placeholderUrl)
        .crossfade(true)
        .build()

    ZoomableAsyncImage(
        model = request,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
        onLongClick = onLongClick
    )
}