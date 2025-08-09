package io.sadwhy.party

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.sadwhy.party.core.navigation.NavRoute
import io.sadwhy.party.core.navigation.NavControl
import io.sadwhy.party.core.navigation.NavControllerProvider
import io.sadwhy.party.core.ui.composable.dialog.DialogHandler
import io.sadwhy.party.screen.home.HomeScreen
import io.sadwhy.party.screen.library.LibraryScreen
import io.sadwhy.party.screen.search.SearchScreen

data class BottomNavItem(
    val destination: NavRoute,
    val label: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
)

fun getBottomNavItems() = listOf(
    BottomNavItem(
        destination = NavRoute.Home,
        label = "Home",
        iconSelected = R.drawable.ic_home_filled,
        iconUnselected = R.drawable.ic_home_outline
    ),
    BottomNavItem(
        destination = NavRoute.Search,
        label = "Search",
        iconSelected = R.drawable.ic_search_filled,
        iconUnselected = R.drawable.ic_search_outline
    ),
    BottomNavItem(
        destination = NavRoute.Library,
        label = "Library",
        iconSelected = R.drawable.ic_bookmark_filled,
        iconUnselected = R.drawable.ic_bookmark_outline
    )
)

@Composable
fun MainScreen() {
    val navController = NavControllerProvider.getNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentDestination = NavRoute.fromRoute(currentRoute)
    val bottomNavItems = getBottomNavItems()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    navController = navController,
                    currentDestination = currentDestination,
                    bottomNavItems = bottomNavItems
                )
            }
        ) { innerPadding ->
            DialogHandler()
            NavHostContent(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun BottomBar(
    navController: androidx.navigation.NavHostController,
    currentDestination: NavRoute?,
    bottomNavItems: List<BottomNavItem>
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination == item.destination
            NavigationBarItem(
                icon = {
                    Crossfade(targetState = isSelected) { selected ->
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
                    if (!isSelected) {
                        NavControl.goToAndClearBackStack(navController, item.destination)
                    }
                }
            )
        }
    }
}


@Composable
private fun NavHostContent(
    navController: androidx.navigation.NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route,
        modifier = modifier
    ) {
        composable(NavRoute.Home.route) { HomeScreen() }
        composable(NavRoute.Search.route) { SearchScreen("Search", "Compose Search Screen") }
        composable(NavRoute.Library.route) { LibraryScreen() }
    }
}