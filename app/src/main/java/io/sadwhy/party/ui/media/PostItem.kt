package io.sadwhy.party.ui.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import coil3.compose.AsyncImage
import io.sadwhy.party.R
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.data.model.Attachment

@Composable
fun PostItem(
    nullablePost: Post?,
    domain: String,
    onImageDoubleClick: (Attachment) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        nullablePost?.let { post ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PostHeader(post)
                PostText(post)
                PostAttachments(
                    post = post,
                    domain = domain,
                    onImageDoubleClick = onImageDoubleClick
                )
            }
        } ?: LoadingPlaceholder()
    }
}

@Composable
private fun PostHeader(post: Post) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = "https://img.kemono.su/icons/${post.service}/${post.user}",
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = post.user,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = { /* Handle menu click */ }) {
            Icon(
                painter = painterResource(R.drawable.ic_round_dots_vertical),
                contentDescription = "More options"
            )
        }
    }
}

@Composable
private fun PostText(post: Post) {
    if (!post.title.isNullOrEmpty()) {
        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium
        )
    }
    val bodyText = when {
        !post.substring.isNullOrEmpty() -> post.substring
        !post.content.isNullOrEmpty() -> post.content
        else -> null
    }
    bodyText?.let {
        Text(
            text = it,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PostAttachments(
    post: Post,
    domain: String,
    onImageDoubleClick: (Attachment) -> Unit
) {
    LazyColumn {
        items(post.attachments) { attachment ->
            ZoomableAttachmentImage(
                domain = "kemono",
                a = attachment,
                onDoubleClick = { }
            )
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}