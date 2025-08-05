package io.sadwhy.party.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object NavControllerProvider {
    @Composable
    fun getNavController(): NavHostController {
        return rememberNavController()
    }
}