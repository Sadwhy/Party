package io.sadwhy.party.core.navigation

sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object Search : NavRoute("search")
    object Library : NavRoute("library")

    companion object {
        fun fromRoute(route: String?): NavRoute? = when (route) {
            Home.route -> Home
            Search.route -> Search
            Library.route -> Library
            else -> null
        }
    }
}