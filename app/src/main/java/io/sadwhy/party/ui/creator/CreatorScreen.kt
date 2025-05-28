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
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import coil3.compose.AsyncImage
import io.sadwhy.party.data.model.Post
import io.sadwhy.party.data.model.Creator
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    post: Post,
    creator: Creator?,
    onBackClick: () -> Unit = {}
) {
    val name = remember(creator, post) { creator?.name ?: post.user }
    
    val tabs = remember { listOf("Posts", "Media", "About") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    
    // Calculate collapse progress for animations
    val density = LocalDensity.current
    val collapseRange = with(density) { 200.dp.toPx() } // Banner height
    val scrollOffset = remember {
        derivedStateOf {
            min(listState.firstVisibleItemScrollOffset.toFloat(), collapseRange)
        }
    }
    val collapseProgress = remember {
        derivedStateOf {
            (scrollOffset.value / collapseRange).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsibleTopBar(
                title = name,
                onBackClick = onBackClick,
                collapseProgress = collapseProgress.value,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Banner and Profile Header
            item(key = "header") {
                CreatorHeader(
                    post = post,
                    name = name,
                    collapseProgress = collapseProgress.value
                )
            }
            
            // Tab Row
            item(key = "tabs") {
                CreatorTabRow(
                    tabs = tabs,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )
            }
            
            // Content based on selected tab
            item(key = "content_$selectedTabIndex") {
                CreatorTabContent(selectedTabIndex = selectedTabIndex)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollapsibleTopBar(
    title: String,
    onBackClick: () -> Unit,
    collapseProgress: Float,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        title = { 
            Text(
                text = title,
                modifier = Modifier.graphicsLayer {
                    alpha = 1f - collapseProgress
                }
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack, 
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle menu */ }) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White
                )
            }
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    )
}

@Composable
private fun CreatorHeader(
    post: Post,
    name: String,
    collapseProgress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Banner Image with parallax effect
        AsyncImage(
            model = "https://img.kemono.su/banners/${post.service}/${post.user}",
            contentDescription = "Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .graphicsLayer {
                    translationY = collapseProgress * 100f
                    alpha = 1f - (collapseProgress * 0.5f)
                },
            contentScale = ContentScale.Crop
        )
        
        // Dark overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black.copy(alpha = 0.3f))
        )
        
        // Profile section at bottom
        ProfileSection(
            post = post,
            name = name,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .graphicsLayer {
                    translationY = collapseProgress * -50f
                    alpha = 1f - collapseProgress
                }
        )
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
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = "https://img.kemono.su/icons/${post.service}/${post.user}",
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "Favorites: -1",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun CreatorTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
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
        
        Spacer(modifier = Modifier.height(1000.dp))
    }
}

@Composable
private fun AboutContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("About Content", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This is the about section with creator information and details.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(1000.dp))
    }
}