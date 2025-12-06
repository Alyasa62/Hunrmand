package com.example.hunrmand.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hunrmand.data.source.local.dao.JobDao
import com.example.hunrmand.data.source.local.dao.UserDao
import com.example.hunrmand.data.source.local.entity.JobEntity
import com.example.hunrmand.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, JobEntity::class], version = 2, exportSchema = false)
abstract class HunrmandDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun jobDao(): JobDao
}
