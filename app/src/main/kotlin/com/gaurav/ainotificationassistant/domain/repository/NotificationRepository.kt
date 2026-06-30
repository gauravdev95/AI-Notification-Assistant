package com.gaurav.ainotificationassistant.domain.repository

import com.gaurav.ainotificationassistant.domain.model.NotificationItem
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getRecentNotifications(limit: Int): Flow<List<NotificationItem>>
    fun getHighPriorityNotifications(limit: Int): Flow<List<NotificationItem>>
    fun getMediumPriorityNotifications(limit: Int): Flow<List<NotificationItem>>
    fun getByPackage(packageName: String): Flow<List<NotificationItem>>
    fun searchByText(query: String): Flow<List<NotificationItem>>
    fun getStarredNotifications(): Flow<List<NotificationItem>>
    fun getNotificationsByDateRange(startTime: Long, endTime: Long): Flow<List<NotificationItem>>
    fun getNotificationsCount(): Flow<Int>
    suspend fun saveNotification(notification: NotificationItem)
    suspend fun updateNotification(notification: NotificationItem)
    suspend fun deleteNotification(id: Long)
    suspend fun markAsRead(id: Long)
    suspend fun toggleStar(id: Long, starred: Boolean)
    suspend fun archiveNotification(id: Long)
    suspend fun deleteOlderThan(beforeTimestamp: Long): Int
    suspend fun deleteAll()
    suspend fun getTotalSize(): Long
}
