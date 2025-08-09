package io.sadwhy.party.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.sadwhy.party.screen.creator.CreatorProfile
import io.sadwhy.party.screen.creator.CreatorScreen
import io.sadwhy.party.screen.home.Home
import io.sadwhy.party.screen.home.HomeScreen
import io.sadwhy.party.screen.search.Search
import io.sadwhy.party.screen.search.SearchScreen
import io.sadwhy.party.screen.library.Library
import io.sadwhy.party.screen.library.LibraryScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(), modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Home, modifier = modifier) {

        composable<Home> {
            HomeScreen()
        }

        composable<Search> { entry ->
            val args = entry.toRoute<Search>()
            SearchScreen(title = args.title, text = args.text)
        }

        composable<Library> {
            LibraryScreen()
        }

        composable<CreatorProfile> {
            val args = entry.toRoute<CreatorProfile>()
            CreatorScreen(post = args.post, creator = args.creator)
        }
    }
}