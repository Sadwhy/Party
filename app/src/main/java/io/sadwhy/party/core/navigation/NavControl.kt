package io.sadwhy.party.core.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

object NavControl {
    /**
     * Navigate to a screen in a type-safe way.
     */
    fun goTo(navController: NavHostController, destination: NavRoute, builder: (NavOptionsBuilder.() -> Unit)? = null) {
        if (builder != null) {
            navController.navigate(destination.route, builder)
        } else {
            navController.navigate(destination.route)
        }
    }

    /**
     * Go back to the previous screen in the stack.
     */
    fun goBack(navController: NavHostController) {
        navController.popBackStack()
    }

    /**
     * Go to the next screen, and remove the previous screen from the stack (single top).
     */
    fun goToSingleTop(navController: NavHostController, destination: NavRoute) {
        navController.navigate(destination.route) {
            launchSingleTop = true  // Avoids duplicate destinations
        }
    }

    /**
     * Go to a new screen and remove all previous screens from the stack.
     */
    fun goToAndClearBackStack(navController: NavHostController, destination: NavRoute) {
        navController.navigate(destination.route) {
            popUpTo(0) { inclusive = true }
        }
    }
}