package com.example.hunrmand.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hunrmand.data.source.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<NotificationEntity>>
}
