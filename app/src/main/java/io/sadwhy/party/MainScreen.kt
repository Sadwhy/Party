package io.sadwhy.party

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hierarchy
import io.sadwhy.party.core.navigation.MainNavHost

data class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem("home", "Home", R.drawable.ic_home_filled, R.drawable.ic_home_outline),
        BottomNavItem("search", "Search", R.drawable.ic_search_filled, R.drawable.ic_search_outline),
        BottomNavItem("library", "Library", R.drawable.ic_bookmark_filled, R.drawable.ic_bookmark_outline)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScaffold(navController = navController, bottomNavItems = bottomNavItems)
    }
}

@Composable
private fun MainScaffold(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = bottomNavItems)
        }
    ) { innerPadding ->
        MainNavHost(navController = navController, innerPadding = innerPadding)
    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

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
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}