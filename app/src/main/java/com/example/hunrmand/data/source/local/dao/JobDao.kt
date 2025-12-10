package com.example.hunrmand.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hunrmand.data.source.local.entity.JobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)

    @Query("SELECT * FROM jobs ORDER BY createdAt DESC")
    suspend fun getAllJobs(): List<JobEntity>

    @Query("SELECT * FROM jobs WHERE userId = :userId ORDER BY createdAt DESC")
    fun getJobsByUserId(userId: String): Flow<List<JobEntity>>
}
