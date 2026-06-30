package com.gaurav.ainotificationassistant.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat_messages",
    indices = [
        Index("session_id"),
        Index("timestamp")
    ]
)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo("session_id")
    val sessionId: String,

    @ColumnInfo("role")
    val role: String,

    @ColumnInfo("content")
    val content: String,

    @ColumnInfo("timestamp")
    val timestamp: Long,

    @ColumnInfo("is_streaming")
    val isStreaming: Boolean = false,

    @ColumnInfo("token_count")
    val tokenCount: Int? = null
)
