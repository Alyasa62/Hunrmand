package com.example.hunrmand.domain.repository

import com.example.hunrmand.domain.model.Job
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    suspend fun createJob(job: Job): Result<Unit>
    suspend fun getJobs(): List<Job>
    suspend fun getJobsByUserId(userId: String): List<Job>
}
