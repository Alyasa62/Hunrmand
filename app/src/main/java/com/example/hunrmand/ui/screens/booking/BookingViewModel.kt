package com.example.hunrmand.ui.screens.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.domain.model.Job
import com.example.hunrmand.domain.repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookingViewModel(
    private val jobRepository: JobRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _bookings = MutableStateFlow<List<Job>>(emptyList())
    val bookings: StateFlow<List<Job>> = _bookings.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchBookings()
    }

    private fun fetchBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            val userId = sessionManager.userId.first()
            val role = sessionManager.userRole.first()

            if (userId != null) {
                if (role == "WORKER" || role == "Worker") {
                     // Empty for now as per requirement analysis
                     _bookings.value = emptyList() 
                     _isLoading.value = false
                } else {
                     jobRepository.getJobsByUserId(userId).collect { jobs ->
                         _bookings.value = jobs
                         _isLoading.value = false
                     }
                }
            } else {
                _isLoading.value = false
            }
        }
    }
}
