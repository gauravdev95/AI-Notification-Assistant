package com.gaurav.ainotificationassistant.domain.model

data class NotificationItem(
    val id: Long,
    val packageName: String,
    val appLabel: String,
    val title: String?,
    val body: String?,
    val timestamp: Long,
    val importance: Importance,
    val isOngoing: Boolean,
    val category: String?,
    val groupKey: String?,
    val isRead: Boolean,
    val isStarred: Boolean = false,
    val isArchived: Boolean = false
)
