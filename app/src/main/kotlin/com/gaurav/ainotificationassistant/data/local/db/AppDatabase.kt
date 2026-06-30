package com.gaurav.ainotificationassistant.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gaurav.ainotificationassistant.data.local.db.dao.ChatMessageDao
import com.gaurav.ainotificationassistant.data.local.db.dao.NotificationDao
import com.gaurav.ainotificationassistant.data.local.db.entity.ChatMessageEntity
import com.gaurav.ainotificationassistant.data.local.db.entity.NotificationEntity

@Database(
    entities = [
        NotificationEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
    abstract fun chatMessageDao(): ChatMessageDao
}
