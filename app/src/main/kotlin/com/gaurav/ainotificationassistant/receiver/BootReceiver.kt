package com.gaurav.ainotificationassistant.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import com.gaurav.ainotificationassistant.service.NotificationListenerServiceImpl
import com.gaurav.ainotificationassistant.worker.CleanupWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            try {
                val componentName = ComponentName(context, NotificationListenerServiceImpl::class.java)
                NotificationListenerService.requestRebind(componentName)
                CleanupWorker.schedule(context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
