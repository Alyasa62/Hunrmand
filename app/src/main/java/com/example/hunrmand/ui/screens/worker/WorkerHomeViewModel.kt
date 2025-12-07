package com.example.hunrmand.ui.screens.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.domain.repository.LocationRepository
import com.example.hunrmand.domain.repository.WorkerRepository
import com.example.hunrmand.domain.usecase.worker.JobWithDistance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class WorkerHomeViewModel(
    private val workerRepository: WorkerRepository,
    private val locationRepository: LocationRepository,
    private val sessionManager: SessionManager,
    private val userDao: com.example.hunrmand.data.source.local.dao.UserDao,
    private val jobDao: com.example.hunrmand.data.source.local.dao.JobDao // To check total system jobs
) : ViewModel() {

    private val _jobs = MutableStateFlow<List<JobWithDistance>>(emptyList())
    val jobs: StateFlow<List<JobWithDistance>> = _jobs.asStateFlow()

    private val _uiState = MutableStateFlow<String?>(null) // "EMPTY_SYSTEM", "EMPTY_MATCHES", or null (Success)
    val uiState: StateFlow<String?> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = null
            
            // 1. Check System Jobs
            val totalJobs = jobDao.getAllJobs()
            if (totalJobs.isEmpty()) {
                _uiState.value = "EMPTY_SYSTEM"
                _isLoading.value = false
                return@launch
            }

            // 2. Fetch Current User Profile
            val userId = sessionManager.userId.firstOrNull()
            if (userId != null) {
                val user = userDao.getUserById(userId)
                
                // Fallback coordinates (Mianwali) if user has none
                val userLat = user?.latitude ?: 32.5839
                val userLng = user?.longitude ?: 71.5370
                val userCategory = user?.profession ?: "Plumber" // Default to Plumber for testing if null

                // 3. Fetch Matching Jobs
                val matchedJobs = workerRepository.getJobsForWorker(userLat, userLng, userCategory)
                
                if (matchedJobs.isEmpty()) {
                    _uiState.value = "EMPTY_MATCHES"
                } else {
                    _jobs.value = matchedJobs
                }
            } else {
                 _uiState.value = "EMPTY_MATCHES" // No user, cant match
            }
            _isLoading.value = false
        }
    }
}
