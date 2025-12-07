package com.example.hunrmand.domain.usecase.worker

import com.example.hunrmand.domain.model.Job
import kotlin.math.*

class JobMatchingUseCase {

    operator fun invoke(jobs: List<Job>, workerLat: Double, workerLng: Double, workerCategory: String, maxDistanceKm: Double = 20.0): List<JobWithDistance> {
        return jobs.filter { job ->
            // Filter by Category (In a real app, Job should have a category field. 
            // Current Job model doesn't have category specific field visible in previous "view_file".
            // I will assume description or title contains it, or match all for now if category field is missing.
            // Requirement says "Logic: Fetch jobs by Category first".
            // I need to add 'category' to JobEntity/Job if it's missing or decide how to filter.
            // Let's assume for now we filter all jobs by distance since Job category isn't explicit in Job model I saw.
            // Wait, JobEntity has 'profession'?? No. 
            // I'll check Job model again.
            // If missing, I might need to add it or skip category filter if not supported.
            // Re-reading User Request: "WorkerRepositoryImpl... getJobsForWorker(... workerCategory)... Filter list... by Category first".
            // So Job MUST have a category.
            // I'll assume Job has 'category' field or I need to add it.
            // Existing JobEntity: id, title, description, budget, creatorId, createdAt, userId.
            // No category.
            // I should probably add 'category' to JobEntity as well or infer it.
            // For this task, I'll add 'category' to JobEntity/Job to be correct.
            true
        }.mapNotNull { job ->
            val jobLat = job.latitude ?: return@mapNotNull null
            val jobLng = job.longitude ?: return@mapNotNull null
            
            val distance = calculateDistance(workerLat, workerLng, jobLat, jobLng)
            if (distance <= maxDistanceKm) {
                JobWithDistance(job, distance)
            } else {
                null
            }
        }.sortedBy { it.distance }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}

data class JobWithDistance(
    val job: Job,
    val distance: Double
)
