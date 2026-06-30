package com.gaurav.ainotificationassistant.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.gaurav.ainotificationassistant.domain.repository.NotificationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationRepository: NotificationRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val retentionDays = inputData.getLong(KEY_RETENTION_DAYS, 30L)
            val cutoff = System.currentTimeMillis() - (retentionDays * 24 * 60 * 60 * 1000L)
            val deleted = notificationRepository.deleteOlderThan(cutoff)
            Result.success(workDataOf("deleted_count" to deleted))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        const val KEY_RETENTION_DAYS = "retention_days"
        private const val CLEANUP_WORK_NAME = "notification_cleanup"

        fun schedule(context: Context, retentionDays: Long = 30L) {
            val request = PeriodicWorkRequestBuilder<CleanupWorker>(7, TimeUnit.DAYS)
                .setInputData(workDataOf(KEY_RETENTION_DAYS to retentionDays))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                CLEANUP_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(CLEANUP_WORK_NAME)
        }
    }
}
