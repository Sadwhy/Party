package io.sadwhy.party

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import io.sadwhy.party.R

data class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val iconSelected: Int,
    @DrawableRes val iconUnselected: Int
)

class MainViewModel : ViewModel() {
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
    
    fun navigateToDestination(
        navController: androidx.navigation.NavHostController,
        route: String
    ) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}