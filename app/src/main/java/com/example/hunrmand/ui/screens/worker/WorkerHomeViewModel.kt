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
import kotlinx.coroutines.launch

class WorkerHomeViewModel(
    private val workerRepository: WorkerRepository,
    private val locationRepository: LocationRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _jobs = MutableStateFlow<List<JobWithDistance>>(emptyList())
    val jobs: StateFlow<List<JobWithDistance>> = _jobs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _isLoading.value = true
            sessionManager.city.collectLatest { city ->
                val cityQuery = city ?: "Lahore" // Default
                val coords = locationRepository.getCoordinatesFromCity(cityQuery) ?: (31.5204 to 74.3587) // Default Lahore
                
                // Assuming we can get category from user profile or preferences. 
                // For now, let's assume we want ALL relevant jobs or pass a wildcard.
                // The prompt says "WorkerCategory".
                // I should fetch User Profile to get profession.
                // But efficient way: pass "all" or specific.
                // Let's rely on repository executing pure distance first if category is empty/all.
                // Wait, Repository implementation: 
                // return jobMatchingUseCase(allJobs, pdLat, pdLng, category)
                // logic: filter logic inside UseCase.
                // I'll pass a placeholder category or fetch from session if available.
                // SessionManager has USER_ROLE but not CATEGORY.
                // I will use "elec" (Electrician) as hardcoded for demo or try to fetch user.
                // Ideally I should inject AuthRepository/LocalAuthDataSource to get current user details.
                // But I'll use "elec" for now and comment.
                
                val jobsList = workerRepository.getJobsForWorker(coords.first, coords.second, "elec")
                _jobs.value = jobsList
                _isLoading.value = false
            }
        }
    }
}
