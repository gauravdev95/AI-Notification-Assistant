package com.gaurav.ainotificationassistant.domain.engine

import android.app.Notification
import com.gaurav.ainotificationassistant.domain.model.Importance
import javax.inject.Inject

class ImportanceEngine @Inject constructor() {

    private val highPriorityPackages = setOf(
        "com.android.phone",
        "com.google.android.dialer",
        "com.google.android.gms"
    )

    private val blockedPackages = setOf(
        "com.android.systemui",
        "android"
    )

    private val importantKeywords = listOf(
        "urgent", "critical", "alert", "otp", "verify", "code", "payment",
        "expires", "missed call", "interview", "placement", "offer", "recruiter",
        "bank", "confirm", "verification", "password", "login", "security"
    )

    fun classify(
        packageName: String,
        category: String?,
        title: String?,
        body: String?,
        isOngoing: Boolean,
        notificationPriority: Int
    ): Importance {
        if (packageName in blockedPackages) return Importance.LOW
        if (packageName in highPriorityPackages) return Importance.HIGH

        var score = 0

        score += when (category) {
            Notification.CATEGORY_CALL, Notification.CATEGORY_ALARM -> 3
            Notification.CATEGORY_MESSAGE, Notification.CATEGORY_EMAIL -> 2
            Notification.CATEGORY_REMINDER, Notification.CATEGORY_EVENT -> 2
            Notification.CATEGORY_PROGRESS, Notification.CATEGORY_SERVICE -> 0
            else -> 1
        }

        val text = "${title.orEmpty()} ${body.orEmpty()}".lowercase()
        score += computeTextScore(text)

        if (notificationPriority >= 1) score += 1
        if (!isOngoing) score += 1

        return when {
            score >= 4 -> Importance.HIGH
            score >= 2 -> Importance.MEDIUM
            else -> Importance.LOW
        }
    }

    private fun computeTextScore(text: String): Int {
        val matches = importantKeywords.count { keyword -> keyword in text }
        return matches.coerceAtMost(2)
    }
}
