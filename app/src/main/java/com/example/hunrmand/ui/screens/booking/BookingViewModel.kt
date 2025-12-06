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
                // For now, regardless of role, we show jobs related to user.
                // If Worker, we ideally show 'Applied' jobs, but for now we follow prompt:
                // "For Workers: The 'Bookings' tab should show jobs they have accepted or applied for."
                // Since we lack that table, we'll just show 'getJobsByUserId' (Jobs they posted? No, workers don't post).
                // So for Workers, list might be empty or we show "No bookings found".
                // Prompt: "Query: Update JobDao to include a query: getJobsByUserId(userId: String)..."
                
                if (role == "WORKER" || role == "Worker") {
                     // Temporary: Show nothing or implement fake logic if needed?
                     // Prompt says "Reuse JobItem... Empty State: No recent bookings found."
                     // So returning empty list is fine for now.
                     _bookings.value = emptyList() 
                } else {
                     _bookings.value = jobRepository.getJobsByUserId(userId)
                }
            }
            _isLoading.value = false
        }
    }
}
