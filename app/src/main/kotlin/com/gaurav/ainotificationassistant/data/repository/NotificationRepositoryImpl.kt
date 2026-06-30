package com.gaurav.ainotificationassistant.data.repository

import com.gaurav.ainotificationassistant.data.local.db.dao.NotificationDao
import com.gaurav.ainotificationassistant.data.local.db.entity.NotificationEntity
import com.gaurav.ainotificationassistant.domain.model.Importance
import com.gaurav.ainotificationassistant.domain.model.NotificationItem
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getRecentNotifications(limit: Int): Flow<List<NotificationItem>> =
        notificationDao.getRecent(limit).map { entities -> entities.map { it.toDomain() } }

    override fun getHighPriorityNotifications(limit: Int): Flow<List<NotificationItem>> =
        notificationDao.getByImportance(Importance.HIGH.name, limit)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getMediumPriorityNotifications(limit: Int): Flow<List<NotificationItem>> =
        notificationDao.getByImportance(Importance.MEDIUM.name, limit)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getByPackage(packageName: String): Flow<List<NotificationItem>> =
        notificationDao.getByPackage(packageName).map { entities -> entities.map { it.toDomain() } }

    override fun searchByText(query: String): Flow<List<NotificationItem>> =
        notificationDao.searchByText(query).map { entities -> entities.map { it.toDomain() } }

    override fun getStarredNotifications(): Flow<List<NotificationItem>> =
        notificationDao.getStarred().map { entities -> entities.map { it.toDomain() } }

    override fun getNotificationsByDateRange(startTime: Long, endTime: Long): Flow<List<NotificationItem>> =
        notificationDao.getByDateRange(startTime, endTime)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getNotificationsCount(): Flow<Int> = notificationDao.getCount()

    override suspend fun saveNotification(notification: NotificationItem) {
        notificationDao.insert(notification.toEntity())
    }

    override suspend fun updateNotification(notification: NotificationItem) {
        notificationDao.update(notification.toEntity())
    }

    override suspend fun deleteNotification(id: Long) {
        notificationDao.delete(NotificationEntity(id = id, packageName = "", appLabel = "", timestamp = 0, importance = "LOW"))
    }

    override suspend fun markAsRead(id: Long) {
        notificationDao.markRead(id)
    }

    override suspend fun toggleStar(id: Long, starred: Boolean) {
        notificationDao.updateStarred(id, starred)
    }

    override suspend fun archiveNotification(id: Long) {
        notificationDao.archive(id)
    }

    override suspend fun deleteOlderThan(beforeTimestamp: Long): Int =
        notificationDao.deleteOlderThan(beforeTimestamp)

    override suspend fun deleteAll() {
        notificationDao.deleteAll()
    }

    override suspend fun getTotalSize(): Long {
        return notificationDao.getTotalSize() ?: 0L
    }

    private fun NotificationEntity.toDomain(): NotificationItem =
        NotificationItem(
            id = id,
            packageName = packageName,
            appLabel = appLabel,
            title = title,
            body = body,
            timestamp = timestamp,
            importance = Importance.valueOf(importance),
            isOngoing = isOngoing,
            category = category,
            groupKey = groupKey,
            isRead = isRead,
            isStarred = isStarred,
            isArchived = isArchived
        )

    private fun NotificationItem.toEntity(): NotificationEntity =
        NotificationEntity(
            id = id,
            packageName = packageName,
            appLabel = appLabel,
            title = title,
            body = body,
            timestamp = timestamp,
            importance = importance.name,
            isOngoing = isOngoing,
            category = category,
            groupKey = groupKey,
            isRead = isRead,
            isStarred = isStarred,
            isArchived = isArchived
        )
}
