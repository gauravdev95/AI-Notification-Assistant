package com.gaurav.ainotificationassistant.domain.usecase

import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import javax.inject.Inject

class ClearOldNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {
    suspend operator fun invoke(retentionDays: Long): Int {
        val cutoffTime = System.currentTimeMillis() - (retentionDays * 24 * 60 * 60 * 1000L)
        return notificationRepository.deleteOlderThan(cutoffTime)
    }
}
