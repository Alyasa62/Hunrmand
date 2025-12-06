package com.example.hunrmand.ui.screens.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.model.Job
import com.example.hunrmand.domain.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PostJobViewModel(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _postJobState = MutableStateFlow<Result<Unit>?>(null)
    val postJobState: StateFlow<Result<Unit>?> = _postJobState.asStateFlow()
    
    private val _allJobs = MutableStateFlow<List<Job>>(emptyList())
    val allJobs: StateFlow<List<Job>> = _allJobs.asStateFlow()

    init {
        fetchAllJobs()
    }

    private fun fetchAllJobs() {
        viewModelScope.launch {
            _allJobs.value = jobRepository.getJobs()
        }
    }

    fun postJob(title: String, description: String, budget: Double, creatorId: String) {
        viewModelScope.launch {
            val job = Job(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                budget = budget,
                creatorId = creatorId
            )
            _postJobState.value = jobRepository.createJob(job)
        }
    }
    
    fun resetPostJobState() {
        _postJobState.value = null
    }
}
