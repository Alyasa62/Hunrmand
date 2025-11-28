package com.example.hunrmand.domain.model

data class Worker(
    val id: String,
    val name: String,
    val categoryId: String, // Links this worker to a category (e.g., "elec")
    val rating: Double,
    val city: String,
    val hourlyRate: String,
    val imageUrl: String? = null, // Placeholder for real image URL
    val isTopPick: Boolean = false
)