package io.sadwhy.party

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import io.sadwhy.party.R
import io.sadwhy.party.screen.home.HomeScreen
import io.sadwhy.party.screen.library.LibraryScreen
import io.sadwhy.party.screen.search.SearchScreen
import io.sadwhy.party.core.theme.AppTheme

data class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            SystemBarStyle.auto(TRANSPARENT, TRANSPARENT),
            SystemBarStyle.dark(TRANSPARENT)
        )

        setContent {
            AppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "home",
            label = "Home",
            iconSelected = R.drawable.ic_home_filled,
            iconUnselected = R.drawable.ic_home_outline
        ),
        BottomNavItem(
            route = "search",
            label = "Search",
            iconSelected = R.drawable.ic_search_filled,
            iconUnselected = R.drawable.ic_search_outline
        ),
        BottomNavItem(
            route = "library",
            label = "Library",
            iconSelected = R.drawable.ic_bookmark_filled,
            iconUnselected = R.drawable.ic_bookmark_outline
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomNavItems.forEach { item ->
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