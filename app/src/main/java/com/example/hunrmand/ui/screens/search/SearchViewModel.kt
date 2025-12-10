package com.example.hunrmand.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.model.Category
import com.example.hunrmand.domain.repository.WorkerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val workerRepository: WorkerRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _allCategories = MutableStateFlow<List<Category>>(emptyList())
    val allCategories = _allCategories.asStateFlow()

    val filteredCategories: StateFlow<List<Category>> = combine(_searchQuery, _allCategories) { query, categories ->
        if (query.isBlank()) {
            categories
        } else {
            val lowerQuery = query.lowercase().trim()
            categories.filter { category ->
                category.name.lowercase().contains(lowerQuery)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _allCategories.value = workerRepository.getCategories()
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

