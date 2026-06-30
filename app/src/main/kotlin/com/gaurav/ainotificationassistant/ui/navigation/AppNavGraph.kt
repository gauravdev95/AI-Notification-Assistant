package com.gaurav.ainotificationassistant.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gaurav.ainotificationassistant.ui.screen.chat.ChatScreen
import com.gaurav.ainotificationassistant.ui.screen.notifications.NotificationsScreen
import com.gaurav.ainotificationassistant.ui.screen.permission.PermissionScreen
import com.gaurav.ainotificationassistant.ui.screen.settings.SettingsScreen

fun NavGraphBuilder.appNavGraph(
    navController: NavHostController,
    onRecheckNotificationAccess: () -> Unit
) {
    composable(Screen.Permission.route) {
        PermissionScreen(onPermissionGranted = {
            onRecheckNotificationAccess()
            navController.navigate(Screen.Chat.route) {
                popUpTo(Screen.Permission.route) { inclusive = true }
            }
        })
    }

    composable(Screen.Chat.route) {
        ChatScreen(
            onNavigateToNotifications = {
                navController.navigate(Screen.Notifications.route)
            },
            onNavigateToSettings = {
                navController.navigate(Screen.Settings.route)
            }
        )
    }

    composable(Screen.Notifications.route) {
        NotificationsScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    composable(Screen.Settings.route) {
        SettingsScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
