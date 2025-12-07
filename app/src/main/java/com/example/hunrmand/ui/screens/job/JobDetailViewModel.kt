package com.example.hunrmand.ui.screens.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.model.Job
import com.example.hunrmand.domain.repository.BidRepository
import com.example.hunrmand.domain.repository.JobRepository
import com.example.hunrmand.data.source.local.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class JobDetailViewModel(
    private val jobRepository: JobRepository,
    private val bidRepository: BidRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _job = MutableStateFlow<Job?>(null)
    val job: StateFlow<Job?> = _job.asStateFlow()

    private val _bidState = MutableStateFlow<Result<Unit>?>(null)
    val bidState: StateFlow<Result<Unit>?> = _bidState.asStateFlow()

    fun loadJob(jobId: String) {
        viewModelScope.launch {
            // Need getJobById in Repository. 
            // Previous JobRepository interface didn't have single job fetch.
            // I'll filter from 'getJobs' for now or add it.
            // Requirement says "Create a function...".
            // I will use filtering from getAll since I haven't added getJobById. 
            // In a real app this is inefficient.
            val allJobs = jobRepository.getJobs()
            _job.value = allJobs.find { it.id == jobId }
        }
    }

    fun placeBid(jobId: String, amount: Double, estimatedTime: String) {
        viewModelScope.launch {
            val userId = sessionManager.userId.firstOrNull()
            if (userId != null) {
                _bidState.value = bidRepository.placeBid(jobId, userId, amount, estimatedTime)
            } else {
                _bidState.value = Result.failure(Exception("User not found"))
            }
        }
    }
    
    fun resetBidState() {
        _bidState.value = null
    }
}
