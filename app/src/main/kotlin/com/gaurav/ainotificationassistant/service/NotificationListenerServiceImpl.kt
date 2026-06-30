package com.gaurav.ainotificationassistant.service

import android.app.Notification
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.gaurav.ainotificationassistant.domain.engine.ImportanceEngine
import com.gaurav.ainotificationassistant.domain.model.Importance
import com.gaurav.ainotificationassistant.domain.model.NotificationItem
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListenerServiceImpl : NotificationListenerService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var importanceEngine: ImportanceEngine

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        if (sbn.isOngoing) return

        serviceScope.launch {
            try {
                val notification = sbn.notification ?: return@launch
                val packageName = sbn.packageName
                val title = notification.extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
                val text = notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
                val category = notification.category

                val importance = importanceEngine.classify(
                    packageName = packageName,
                    category = category,
                    title = title,
                    body = text,
                    isOngoing = sbn.isOngoing,
                    notificationPriority = notification.priority
                )

                if (importance != Importance.LOW) {
                    val item = NotificationItem(
                        id = 0,
                        packageName = packageName,
                        appLabel = getAppLabel(packageName),
                        title = title,
                        body = text,
                        timestamp = sbn.postTime,
                        importance = importance,
                        isOngoing = sbn.isOngoing,
                        category = category,
                        groupKey = notification.group,
                        isRead = false,
                        isStarred = false,
                        isArchived = false
                    )
                    notificationRepository.saveNotification(item)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn ?: return
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun getAppLabel(packageName: String): String {
        return try {
            val pm = packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            packageName
        }
    }
}
