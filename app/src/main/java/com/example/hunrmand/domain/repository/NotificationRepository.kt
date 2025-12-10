package com.example.hunrmand.domain.repository

import com.example.hunrmand.data.source.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun sendNotification(notification: NotificationEntity)
    suspend fun sendNotifications(notifications: List<NotificationEntity>)
    fun getNotifications(userId: String): Flow<List<NotificationEntity>>
}
