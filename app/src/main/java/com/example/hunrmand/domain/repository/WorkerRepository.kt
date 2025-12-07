package com.example.hunrmand.domain.repository

import com.example.hunrmand.domain.model.Category
import com.example.hunrmand.domain.model.Worker

interface WorkerRepository {
    fun getCategories(): List<Category>
    fun getWorkersByCategory(categoryId: String): List<Worker>
    fun getWorkerById(workerId: String): Worker?

    fun getTopRatedWorkers(): List<Worker>
    
    suspend fun deleteWorker(workerId: String)
    suspend fun getJobsForWorker(pdLat: Double, pdLng: Double, category: String): List<com.example.hunrmand.domain.usecase.worker.JobWithDistance>
}