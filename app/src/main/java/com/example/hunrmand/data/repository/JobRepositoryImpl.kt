package com.example.hunrmand.data.repository

import com.example.hunrmand.data.source.local.dao.JobDao
import com.example.hunrmand.data.source.local.entity.JobEntity
import com.example.hunrmand.domain.model.Job
import com.example.hunrmand.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JobRepositoryImpl(
    private val jobDao: JobDao
) : JobRepository {

    override suspend fun createJob(job: Job): Result<Unit> {
        return try {
            val jobEntity = JobEntity(
                id = job.id,
                title = job.title,
                description = job.description,
                budget = job.budget,
                creatorId = job.creatorId,
                createdAt = job.date,
                userId = job.creatorId // Assuming userId in JobEntity matches creatorId/User logic
            )
            jobDao.insertJob(jobEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Note: implementation_plan said "getJobs" returns Flow.
    // But previous Tool 399 changed interface to suspend fun getJobs(): List<Job> ??
    // No, split view: Tool 399 output shows:
    // suspend fun getJobs(): List<Job>
    // suspend fun getJobsByUserId(userId: String): List<Job>
    
    // So I must match that interface.
    
    override suspend fun getJobs(): List<Job> {
        return jobDao.getAllJobs().map { entity ->
            Job(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                budget = entity.budget,
                creatorId = entity.creatorId,
                date = entity.createdAt,
                location = "Lahore" // Placeholder
            )
        }
    }

    override suspend fun getJobsByUserId(userId: String): List<Job> {
        return jobDao.getJobsByUserId(userId).map {
            Job(
                id = it.id,
                title = it.title,
                description = it.description,
                budget = it.budget,
                location = "Lahore", // Placeholder
                date = it.createdAt,
                creatorId = it.creatorId // Ensure mapping
            )
        }
    }
}
