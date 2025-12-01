package com.example.hunrmand.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.model.Category
import com.example.hunrmand.domain.model.Worker
import com.example.hunrmand.domain.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val workerRepository: WorkerRepository
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
    }

    // Called when user returns from Map Screen
    fun updateCity(city: String) {
        _currentCity.value = city
        // workerRepository.getWorkersByCity(city)
    }
}