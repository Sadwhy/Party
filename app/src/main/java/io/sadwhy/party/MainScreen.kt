package io.sadwhy.party

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.sadwhy.party.screen.home.HomeScreen
import io.sadwhy.party.screen.library.LibraryScreen
import io.sadwhy.party.screen.search.SearchScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    viewModel.bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            icon = {
                                Crossfade(targetState = isSelected, label = "nav_icon") { selected ->
                                    Icon(
                                        painter = painterResource(
                                            id = if (selected) item.iconSelected else item.iconUnselected
                                        ),
                                        contentDescription = item.label
                                    )
                                }
                            },
                            label = { Text(item.label) },
                            selected = isSelected,
                            onClick = {
                                viewModel.navigateToDestination(navController, item.route)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    HomeScreen()
                }
                composable("search") {
                    SearchScreen("Search", "Compose Search Screen")
                }
                composable("library") {
                    LibraryScreen()
                }
            }
        }
    }
}