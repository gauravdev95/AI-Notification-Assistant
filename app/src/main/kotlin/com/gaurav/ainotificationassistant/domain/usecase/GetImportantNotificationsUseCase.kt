package com.gaurav.ainotificationassistant.domain.usecase

import com.gaurav.ainotificationassistant.domain.model.NotificationItem
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImportantNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    operator fun invoke(limit: Int = 50): Flow<List<NotificationItem>> {
        return notificationRepository.getHighPriorityNotifications(limit)
            .map { highPriority ->
                notificationRepository.getMediumPriorityNotifications(limit).map { medium ->
                    (highPriority + medium).sortedByDescending { it.timestamp }
                }
            }
            .let { notificationRepository.getHighPriorityNotifications(limit) }
    }

    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<NotificationItem>> {
        return notificationRepository.getNotificationsByDateRange(startTime, endTime)
    }

    fun search(query: String): Flow<List<NotificationItem>> {
        return notificationRepository.searchByText(query)
    }
}
