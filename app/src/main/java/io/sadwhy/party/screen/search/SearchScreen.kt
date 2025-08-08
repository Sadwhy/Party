package io.sadwhy.party.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.sadwhy.party.screen.CreatorScreen
import io.sadwhy.party.core.navigation.NavControl
import io.sadwhy.party.core.navigation.NavControllerProvider
import io.sadwhy.party.core.ui.PostItem
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
    
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(title: String, text: String, searchViewModel: SearchViewModel = viewModel()) {
    val navController = NavControllerProvider.getNavController()
    val post by searchViewModel.post.collectAsState()

    val testUrl = "https://kemono.su/patreon/user/5349313/post/115030269"
    var url by remember { mutableStateOf(testUrl) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("Url") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    runFun(url, searchViewModel)
                }
            ) {
                Text("Run Function")
            }

            Spacer(modifier = Modifier.height(24.dp))

            PostItem(
                nullablePost = post,
                domain = "kemono",
                onImageLongClick = {},
                onProfileClick = {
                    CreatorScreen(
                        post = it,
                        onBackClick = { NavControl.backTo(navController) }
                    ) 
                }
            )
        }
    }
}

private fun runFun(url: String, vm: SearchViewModel) {
    val httpUrl: HttpUrl = url.toHttpUrl()
    
    val service = httpUrl.pathSegments[0]
    val user = httpUrl.pathSegments[2]
    val id = httpUrl.pathSegments[4]

    vm.fetchPost(service, user, id)
}