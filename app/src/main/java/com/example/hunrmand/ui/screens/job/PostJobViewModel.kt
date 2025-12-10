package com.example.hunrmand.ui.screens.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.repository.AddressRepository
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.domain.model.Job
import com.example.hunrmand.domain.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

import com.example.hunrmand.domain.usecase.PostJobUseCase

class PostJobViewModel(
    private val jobRepository: JobRepository,
    private val postJobUseCase: PostJobUseCase,
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _postJobState = MutableStateFlow<Result<Unit>?>(null)
    val postJobState: StateFlow<Result<Unit>?> = _postJobState.asStateFlow()
    
    // Form State
    var title = MutableStateFlow("")
    var description = MutableStateFlow("")
    var budget = MutableStateFlow("")
    
    private val _allJobs = MutableStateFlow<List<Job>>(emptyList())
    val allJobs: StateFlow<List<Job>> = _allJobs.asStateFlow()

    init {
        fetchAllJobs()
        fetchDefaultAddress()
    }
    
    // Location State
    var selectedLocation = MutableStateFlow<String?>(null)

    private fun fetchDefaultAddress() {
        viewModelScope.launch {
            val userId = sessionManager.userId.first()
            if (userId != null) {
                val defaultAddress = addressRepository.getDefaultAddress(userId)
                if (defaultAddress != null) {
                    selectedLocation.value = defaultAddress.fullAddress
                }
            }
        }
    }

    private fun fetchAllJobs() {
        viewModelScope.launch {
            _allJobs.value = jobRepository.getJobs()
        }
    }

    fun postJob(creatorId: String, latitude: Double?, longitude: Double?, address: String?) {
        viewModelScope.launch {
            val job = Job(
                id = UUID.randomUUID().toString(),
                title = title.value,
                description = description.value,
                budget = budget.value.toDoubleOrNull() ?: 0.0,
                creatorId = creatorId,
                latitude = latitude,
                longitude = longitude,
                address = address
            )
            _postJobState.value = postJobUseCase(job)
        }
    }
    
    fun resetPostJobState() {
        _postJobState.value = null
        title.value = ""
        description.value = ""
        budget.value = ""
    }
}
