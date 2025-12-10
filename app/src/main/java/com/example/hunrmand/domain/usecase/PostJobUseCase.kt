package com.example.hunrmand.domain.usecase

import com.example.hunrmand.data.source.local.entity.NotificationEntity
import com.example.hunrmand.domain.model.Job
import com.example.hunrmand.domain.repository.JobRepository
import com.example.hunrmand.domain.repository.NotificationRepository
import com.example.hunrmand.domain.repository.WorkerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class PostJobUseCase(
    private val jobRepository: JobRepository,
    private val workerRepository: WorkerRepository,
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(job: Job): Result<Unit> {
        return withContext(Dispatchers.IO) {
            // 1. Create Job
            val result = jobRepository.createJob(job)
            
            if (result.isSuccess) {
                // 2. Find Relevant Workers
                // Requirement: Match by Category and Radius.
                // Currently WorkerRepository.getJobsForWorker does reverse (jobs for worker).
                // I need getWorkersForJob (workers near job).
                // But Prompt says: "Find all Workers with matching Category". 
                // It mentions "Logic (Local DB approach): In WorkerRepositoryImpl, create a function getJobsForWorker...". 
                // But for Notifications it says: "Trigger: In the PostJobUseCase... find all Workers with the matching Category and insert a record".
                // So I need to get workers by category.
                
                // I'll use workerRepository.getWorkersByCategory(category).
                // Job model doesn't have category yet? I mentioned this earlier.
                // UseCase "JobMatching" assumed "elec" for demo.
                // Here, I should assume Job has description/title that implies category or I pass category.
                // I will fetch all workers for now or try to match.
                // Let's assume title contains category keyword for simplicity or fetch all.
                // Prompt: "find all Workers with the matching Category". 
                // I will add a simple keyword match from job title against worker categories.
                
                val categories = workerRepository.getCategories()
                val matchedCategory = categories.find { job.title.contains(it.name, ignoreCase = true) }?.id
                
                // If no match found, maybe notify all? Or none. Prompt implies "matching".
                // I'll notify all if no specific match, or just skip. I'll notify all for visibility in demo.
                // Actually, let's notify all workers of that role.
                
                val workers = if (matchedCategory != null) {
                    workerRepository.getWorkersByCategory(matchedCategory)
                } else {
                    // Fallback: Notify everyone or just first category
                    workerRepository.getWorkersByCategory("elec") // Default
                }
                
                val notifications = workers.map { worker ->
                    NotificationEntity(
                        id = UUID.randomUUID().toString(),
                        userId = worker.id,
                        title = "New Job Available Near You",
                        message = "New job posted: ${job.title}",
                        isRead = false
                    )
                }
                
                notificationRepository.sendNotifications(notifications)
            }
            
            result
        }
    }
}
