package io.sadwhy.party.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import io.sadwhy.party.ui.main.home.HomeScreen
import io.sadwhy.party.ui.main.search.SearchScreen
import io.sadwhy.party.ui.main.library.LibraryFragment

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("search", "Search", Icons.Default.Search),
        BottomNavItem("library", "Library", Icons.Default.Bookmark)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentRoute?.replaceFirstChar { it.uppercase() } ?: "") },
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        },
        bottomBar = {
            BottomNavigation {
                items.forEach { item ->
                    BottomNavigationItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            NavHost(navController, startDestination = "home") {
                composable("home") { HomeScreen() }
                composable("search") { SearchScreen() }
                composable("library") {
                    val context = LocalContext.current
                    val fragmentContainerId = remember { View.generateViewId() }

                    AndroidView(
                        factory = {
                            FragmentContainerView(it).apply {
                                id = fragmentContainerId
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                val fragmentManager = (context as ComponentActivity).supportFragmentManager
                                fragmentManager.commit {
                                    replace<LibraryFragment>(fragmentContainerId, null, bundleOf())
                                }
                            }
                        },
                        update = {}
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)