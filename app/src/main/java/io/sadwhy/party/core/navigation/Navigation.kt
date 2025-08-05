package io.sadwhy.party.core.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.sadwhy.party.screen.home.HomeScreen
import io.sadwhy.party.screen.library.LibraryScreen
import io.sadwhy.party.screen.search.SearchScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("home") {
            HomeScreen()
        }
        composable("search") {
            SearchScreen(title = "Search", description = "Compose Search Screen")
        }
        composable("library") {
            LibraryScreen()
        }
    }
}