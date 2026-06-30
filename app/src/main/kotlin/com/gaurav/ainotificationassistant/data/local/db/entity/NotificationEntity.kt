package com.gaurav.ainotificationassistant.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notifications",
    indices = [
        Index("package_name"),
        Index("importance"),
        Index("timestamp")
    ]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo("package_name")
    val packageName: String,

    @ColumnInfo("app_label")
    val appLabel: String,

    @ColumnInfo("title")
    val title: String?,

    @ColumnInfo("body")
    val body: String?,

    @ColumnInfo("timestamp")
    val timestamp: Long,

    @ColumnInfo("importance")
    val importance: String,

    @ColumnInfo("is_ongoing")
    val isOngoing: Boolean = false,

    @ColumnInfo("category")
    val category: String? = null,

    @ColumnInfo("group_key")
    val groupKey: String? = null,

    @ColumnInfo("is_read")
    val isRead: Boolean = false,

    @ColumnInfo("is_starred")
    val isStarred: Boolean = false,

    @ColumnInfo("is_archived")
    val isArchived: Boolean = false
)
