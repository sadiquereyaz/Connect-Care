package com.reyaz.connectcare.ui.navigation

sealed class NavigationRoute(val route: String ) {
    data object Authentication : NavigationRoute("authentication")
    data object Home : NavigationRoute("home")
}