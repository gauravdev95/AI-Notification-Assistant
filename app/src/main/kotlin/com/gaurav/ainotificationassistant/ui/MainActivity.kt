package com.gaurav.ainotificationassistant.ui

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gaurav.ainotificationassistant.service.NotificationListenerServiceImpl
import com.gaurav.ainotificationassistant.ui.navigation.Screen
import com.gaurav.ainotificationassistant.ui.navigation.appNavGraph
import com.gaurav.ainotificationassistant.ui.theme.AiNotificationAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var hasNotificationAccess = false
    private var shouldRecheck = true

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        val postNotificationPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                checkNotificationAccess()
            }
        }

        setContent {
            AiNotificationAssistantTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    var startDestination by remember { mutableStateOf<String?>(null) }
                    var recheckTrigger by remember { mutableStateOf(0) }

                    LaunchedEffect(shouldRecheck, recheckTrigger) {
                        checkNotificationAccess()
                        startDestination = if (hasNotificationAccess) {
                            Screen.Chat.route
                        } else {
                            Screen.Permission.route
                        }
                        shouldRecheck = false
                    }

                    if (startDestination != null) {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination!!,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            appNavGraph(
                                navController = navController,
                                onRecheckNotificationAccess = {
                                    shouldRecheck = true
                                    recheckTrigger++
                                }
                            )
                        }
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            postNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onResume() {
        super.onResume()
        if (shouldRecheck) {
            checkNotificationAccess()
        }
    }

    private fun checkNotificationAccess() {
        hasNotificationAccess = NotificationManagerCompat.getEnabledListenerPackages(this)
            .contains(packageName)
    }
}
