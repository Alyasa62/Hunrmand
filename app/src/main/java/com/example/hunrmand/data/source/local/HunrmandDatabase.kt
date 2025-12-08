package com.example.hunrmand.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hunrmand.data.source.local.dao.BidDao
import com.example.hunrmand.data.source.local.dao.JobDao
import com.example.hunrmand.data.source.local.dao.NotificationDao
import com.example.hunrmand.data.source.local.dao.UserDao
import com.example.hunrmand.data.source.local.dao.AddressDao
import com.example.hunrmand.data.source.local.entity.AddressEntity
import com.example.hunrmand.data.source.local.entity.BidEntity
import com.example.hunrmand.data.source.local.entity.JobEntity
import com.example.hunrmand.data.source.local.entity.NotificationEntity
import com.example.hunrmand.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, JobEntity::class, BidEntity::class, NotificationEntity::class, AddressEntity::class], version = 4, exportSchema = false)
abstract class HunrmandDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun jobDao(): JobDao
    abstract fun bidDao(): BidDao
    abstract fun notificationDao(): NotificationDao
    abstract fun addressDao(): AddressDao
}
