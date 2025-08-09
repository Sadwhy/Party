package io.sadwhy.party

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import io.party.sadwhy.core.navigation.AppNavHost
import io.party.sadwhy.core.navigation.Navigator
import io.party.sadwhy.core.ui.composable.dialog.DialogHandler
import io.party.sadwhy.screen.home.Home
import io.party.sadwhy.screen.library.Library
import io.party.sadwhy.screen.search.Search

data class BottomNavItem(
    val destination: Any,
    val label: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
)

fun getBottomNavItems(): List<BottomNavItem> = listOf(
    BottomNavItem(
        destination = Home,
        label = "Home",
        iconSelected = R.drawable.ic_home_filled,
        iconUnselected = R.drawable.ic_home_outline
    ),
    BottomNavItem(
        destination = Search(title = "Search", text = "Compose Search Screen"),
        label = "Search",
        iconSelected = R.drawable.ic_search_filled,
        iconUnselected = R.drawable.ic_search_outline
    ),
    BottomNavItem(
        destination = Library,
        label = "Library",
        iconSelected = R.drawable.ic_bookmark_filled,
        iconUnselected = R.drawable.ic_bookmark_outline
    )
)

@Composable
fun MainScreen() {
    val navController = NavControllerHolder.getNavController()

    val navigator = remember(navController) { Navigator(navController) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route

    val bottomNavItems = getBottomNavItems()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(
                    currentRoute = currentRoute,
                    bottomNavItems = bottomNavItems,
                    onNavigate = { destination ->
                        navigator.clearAndNavigate(destination)
                    }
                )
            }
        ) { innerPadding ->
            DialogHandler()
            AppNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun BottomBar(
    currentRoute: String?,
    bottomNavItems: List<BottomNavItem>,
    onNavigate: (Any) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val destinationQualifiedName = item.destination::class.qualifiedName
            val isSelected = currentRoute == destinationQualifiedName

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
                        onNavigate(item.destination)
                    }
                }
            )
        }
    }
}