package com.reyaz.connectcare.ui.navigation

sealed class NavigationRoute(val route: String ) {
    data object Authentication : NavigationRoute("authentication")
    data object ScanDialog : NavigationRoute("scan_dialog")
    data object Home : NavigationRoute("home")
    data object VideoCall : NavigationRoute ("video_call")
}