package com.example.hunrmand.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.model.Category
import com.example.hunrmand.domain.model.Worker
import com.example.hunrmand.domain.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.hunrmand.data.source.local.SessionManager
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(
    private val workerRepository: WorkerRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _topWorkers = MutableStateFlow<List<Worker>>(emptyList())
    val topWorkers = _topWorkers.asStateFlow()

    private val _currentCity = MutableStateFlow("Select City")
    val currentCity = _currentCity.asStateFlow()


    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            //  these will be API calls
            _categories.value = workerRepository.getCategories()
            _topWorkers.value = workerRepository.getTopRatedWorkers()
        }
        
        viewModelScope.launch {
            sessionManager.city.collectLatest { city ->
                 if (!city.isNullOrEmpty()) {
                     _currentCity.value = city
                 }
            }
        }
    }

    // Called when user returns from Map Screen
    fun updateCity(city: String) {
        _currentCity.value = city
        // workerRepository.getWorkersByCity(city)
    }
}