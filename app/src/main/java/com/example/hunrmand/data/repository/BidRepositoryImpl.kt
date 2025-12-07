package com.example.hunrmand.data.repository

import com.example.hunrmand.data.source.local.dao.BidDao
import com.example.hunrmand.data.source.local.entity.BidEntity
import com.example.hunrmand.domain.repository.BidRepository
import java.util.UUID

class BidRepositoryImpl(
    private val bidDao: BidDao
) : BidRepository {
    
    override suspend fun placeBid(jobId: String, workerId: String, amount: Double, estimatedTime: String): Result<Unit> {
        return try {
            val bid = BidEntity(
                id = UUID.randomUUID().toString(),
                jobId = jobId,
                workerId = workerId,
                amount = amount,
                estimatedTime = estimatedTime,
                status = "PENDING"
            )
            bidDao.insertBid(bid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBidsForJob(jobId: String): List<BidEntity> {
        return bidDao.getBidsForJob(jobId)
    }
}
