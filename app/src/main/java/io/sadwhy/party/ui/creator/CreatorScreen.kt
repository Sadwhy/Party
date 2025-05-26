package io.sadwhy.party.ui.creator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.ui.creator.CreatorViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun CreatorScreen(
    post: Post,
    viewModel: CreatorViewModel = viewModel()
) {
    val creator by viewModel.creator.collectAsState()
    val name = creator?.name ?: post.user
    val tabs = listOf("Posts", "Media", "About")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(post) {
        viewModel.fetchCreator(post.service, post.user)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(name) },
                navigationIcon = {
                    IconButton(onClick = { /* handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            BannerSection(post)
            ProfileHeader(name, post)
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
            // FragmentContainer replacement (compose content)
            Box(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(3000.dp)) // simulate long content
            }
        }
    }
}

@Composable
fun BannerSection(post: Post) {
    AsyncImage(
        model = "https://img.kemono.su/banners/${post.service}/${post.user}",
        contentDescription = "Banner",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProfileHeader(name: String, post: Post) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        AsyncImage(
            model = "https://img.kemono.su/icons/${post.service}/${post.user}",
            contentDescription = "Profile",
            modifier = Modifier
                .size(64.dp)
                .padding(end = 16.dp)
        )
        Column {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Favorites: -1", fontSize = 14.sp)
        }
    }
}