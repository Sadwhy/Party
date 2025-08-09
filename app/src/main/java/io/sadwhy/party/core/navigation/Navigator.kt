package io.party.sadwhy.core.navigation

import androidx.navigation.NavHostController

class Navigator(private val navController: NavHostController) {

    fun <T : Any> navigateTo(screen: T) = navController.navigate(screen)

    fun goBack() = navController.popBackStack()

    fun <T : Any> replace(screen: T) {
        navController.navigate(screen) {
            popUpTo(navController.currentDestination?.id ?: return@navigate) { inclusive = true }
        }
    }

    fun <T : Any> clearAndNavigate(screen: T) {
        navController.navigate(screen) {
            popUpTo(0) { inclusive = true }
        }
    }

}