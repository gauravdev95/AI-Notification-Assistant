package com.gaurav.ainotificationassistant.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gaurav.ainotificationassistant.data.local.db.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications WHERE importance = :importance ORDER BY timestamp DESC LIMIT :limit")
    fun getByImportance(importance: String, limit: Int = 50): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 100): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE package_name = :pkg ORDER BY timestamp DESC")
    fun getByPackage(pkg: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchByText(query: String): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE is_starred = 1 ORDER BY timestamp DESC")
    fun getStarred(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE is_archived = 0 AND timestamp > :sinceTimestamp ORDER BY timestamp DESC")
    fun getUnarchived(sinceTimestamp: Long): Flow<List<NotificationEntity>>

    @Query("SELECT COUNT(*) FROM notifications")
    fun getCount(): Flow<Int>

    @Query("SELECT * FROM notifications WHERE timestamp > :startTime AND timestamp < :endTime ORDER BY timestamp DESC")
    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationEntity>)

    @Update
    suspend fun update(notification: NotificationEntity)

    @Delete
    suspend fun delete(notification: NotificationEntity)

    @Query("UPDATE notifications SET is_read = 1 WHERE id = :id")
    suspend fun markRead(id: Long)

    @Query("UPDATE notifications SET is_starred = :starred WHERE id = :id")
    suspend fun updateStarred(id: Long, starred: Boolean)

    @Query("UPDATE notifications SET is_archived = 1 WHERE id = :id")
    suspend fun archive(id: Long)

    @Query("DELETE FROM notifications WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOlderThan(beforeTimestamp: Long): Int

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()

    @Query("SELECT SUM(LENGTH(title) + LENGTH(body)) FROM notifications")
    suspend fun getTotalSize(): Long?
}
