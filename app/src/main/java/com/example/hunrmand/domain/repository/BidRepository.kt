package com.example.hunrmand.domain.repository

import com.example.hunrmand.data.source.local.entity.BidEntity

interface BidRepository {
    suspend fun placeBid(jobId: String, workerId: String, amount: Double, estimatedTime: String): Result<Unit>
    suspend fun getBidsForJob(jobId: String): List<BidEntity>
}
