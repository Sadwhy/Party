package io.sadwhy.party.ui.media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.sadwhy.party.R
import io.sadwhy.party.data.model.Attachment
import io.sadwhy.party.data.model.MediaType
import io.sadwhy.party.data.model.Post

@Composable
fun PostItem(
    nullablePost: Post?,
    domain: String,
    onImageLongClick: () -> Unit
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
                PostAttachments(post, domain, onImageLongClick)
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
    onImageClick: () -> Unit
) {
    post.attachments?.let { attachments ->
        val pagerState = rememberPagerState(pageCount = { attachments.size })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val attachment = attachments[page]
                when (attachment.mediaType) {
                    MediaType.IMAGE -> {
                        ZoomableAttachmentImage(
                            domain = domain,
                            a = attachment,
                            onLongClick = onImageClick
                        )
                    }

                    MediaType.VIDEO -> {
                        Box(Modifier.fillMaxSize()) {
                            // TODO
                        }
                    }

                    MediaType.FILE -> {
                        Box(Modifier.fillMaxSize()) {
                        // TODO
                        }
                    }
                }
            }
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