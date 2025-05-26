package io.sadwhy.party.ui.creator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import io.sadwhy.party.data.model.Post
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    post: Post,
    onBackClick: () -> Unit = {},
    viewModel: CreatorViewModel = viewModel()
) {
    val creator by viewModel.creator.collectAsState()
    val name = remember(creator, post) { creator?.name ?: post.user }
    
    val tabs = remember { listOf("Posts", "Media", "About") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Only fetch creator once when post changes
    LaunchedEffect(post.service, post.user) {
        viewModel.fetchCreator(post.service, post.user)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    
    // Calculate collapse progress based on banner height
    val density = LocalDensity.current
    val bannerHeight = with(density) { 200.dp.toPx() }
    val profileSectionHeight = with(density) { 100.dp.toPx() }
    val totalCollapseRange = bannerHeight + profileSectionHeight
    
    val scrollOffset = remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                listState.firstVisibleItemScrollOffset.toFloat()
            } else {
                totalCollapseRange
            }
        }
    }
    
    val collapseProgress = remember {
        derivedStateOf {
            min(scrollOffset.value / totalCollapseRange, 1f)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Banner Image
            item(key = "banner") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    AsyncImage(
                        model = "https://img.kemono.su/banners/${post.service}/${post.user}",
                        contentDescription = "Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            // Profile Section
            item(key = "profile") {
                ProfileSection(
                    post = post,
                    name = name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                )
            }
            
            // Content based on selected tab
            item(key = "content_$selectedTabIndex") {
                CreatorTabContent(selectedTabIndex = selectedTabIndex)
            }
        }
        
        // Sticky App Bar and Tabs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            // App Bar with banner background when collapsed
            Box {
                // Background banner when collapsed
                if (collapseProgress.value > 0f) {
                    AsyncImage(
                        model = "https://img.kemono.su/banners/${post.service}/${post.user}",
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .graphicsLayer {
                                alpha = collapseProgress.value
                            },
                        contentScale = ContentScale.Crop
                    )
                    
                    // Dark overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .background(Color.Black.copy(alpha = 0.4f * collapseProgress.value))
                    )
                }
                
                TopAppBar(
                    title = { 
                        Text(
                            text = name,
                            color = if (collapseProgress.value > 0.5f) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.Default.ArrowBack, 
                                contentDescription = "Back",
                                tint = if (collapseProgress.value > 0.5f) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle menu */ }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = if (collapseProgress.value > 0.5f) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior
                )
            }
            
            // Sticky Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(
    post: Post,
    name: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = "https://img.kemono.su/icons/${post.service}/${post.user}",
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = "Favorites: -1",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun CreatorTabContent(selectedTabIndex: Int) {
    when (selectedTabIndex) {
        0 -> PostsContent()
        1 -> MediaContent()
        2 -> AboutContent()
    }
}

@Composable
private fun PostsContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Posts Content", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        // Simulate long content for scrolling
        repeat(50) { index ->
            Text(
                text = "Post item $index",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Add extra space to test scrolling
        Spacer(modifier = Modifier.height(1000.dp))
    }
}

@Composable
private fun MediaContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("Media Content", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        repeat(30) { index ->
            Text(
                text = "Media item $index",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AboutContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text("About Content", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This is the about section with creator information and details.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}