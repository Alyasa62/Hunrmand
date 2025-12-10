package com.example.hunrmand.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hunrmand.data.source.local.entity.BidEntity

@Dao
interface BidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBid(bid: BidEntity)

    @Query("SELECT * FROM bids WHERE jobId = :jobId")
    suspend fun getBidsForJob(jobId: String): List<BidEntity>

    @Query("SELECT * FROM bids WHERE workerId = :workerId")
    suspend fun getBidsByWorker(workerId: String): List<BidEntity>
}
