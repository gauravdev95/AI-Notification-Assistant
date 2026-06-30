package com.gaurav.ainotificationassistant.ui.navigation

sealed class Screen(val route: String) {
    object Permission : Screen("permission")
    object Chat : Screen("chat")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}
