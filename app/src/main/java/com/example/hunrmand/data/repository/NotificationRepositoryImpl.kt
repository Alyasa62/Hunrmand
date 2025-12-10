package com.example.hunrmand.data.repository

import com.example.hunrmand.data.source.local.dao.NotificationDao
import com.example.hunrmand.data.source.local.entity.NotificationEntity
import com.example.hunrmand.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(
    private val notificationDao: NotificationDao
) : NotificationRepository {
    
    override suspend fun sendNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    override suspend fun sendNotifications(notifications: List<NotificationEntity>) {
        notificationDao.insertNotifications(notifications)
    }

    override fun getNotifications(userId: String): Flow<List<NotificationEntity>> {
        return notificationDao.getNotificationsForUser(userId)
    }
}
