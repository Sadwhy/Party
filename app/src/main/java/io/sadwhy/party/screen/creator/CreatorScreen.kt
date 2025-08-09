package io.sadwhy.party.screen.creator

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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import io.sadwhy.party.data.model.Creator
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    creatorId: String,
    creatorService: String,
    viewModel: CreatorViewModel = viewModel()
) {
    LaunchedEffect(creatorId, creatorService) {
        viewModel.fetchCreator(creatorId, creatorService)
    }

    val creator by viewModel.creator.collectAsState()

    val tabs = remember { listOf("Posts", "Media", "About") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()

    val density = LocalDensity.current
    val collapseRange = with(density) { 200.dp.toPx() }
    val scrollOffset by remember {
        derivedStateOf {
            min(listState.firstVisibleItemScrollOffset.toFloat(), collapseRange)
        }
    }
    val collapseProgress by remember {
        derivedStateOf {
            (scrollOffset / collapseRange).coerceIn(0f, 1f)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsibleTopBar(
                title = creator?.name ?: "",
                onBackClick = { /* TODO */ },
                collapseProgress = collapseProgress,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        creator?.let {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item("header") {
                    CreatorHeader(
                        creator = it,
                        collapseProgress = collapseProgress
                    )
                }
                item("tabs") {
                    CreatorTabRow(
                        tabs = tabs,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )
                }
                item("content_$selectedTabIndex") {
                    CreatorTabContent(selectedTabIndex)
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
    scrollBehavior: TopAppBarScrollBehavior
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
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO menu action */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
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
    creator: Creator,
    collapseProgress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        AsyncImage(
            model = "https://img.kemono.su/banners/${creator.service}/${creator.id}",
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Black.copy(alpha = 0.3f))
        )
        ProfileSection(
            creator = creator,
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
    creator: Creator,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = "https://img.kemono.su/icons/${creator.service}/${creator.id}",
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
                text = creator.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "Favorites: ${creator.favorited}",
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
        repeat(10) { index ->
            Text("Post item $index", style = MaterialTheme.typography.bodyMedium)
        }
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
        repeat(10) { index ->
            Text("Media item $index", style = MaterialTheme.typography.bodyMedium)
        }
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
            "This is the about section with creator information.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}