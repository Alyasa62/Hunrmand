package com.example.hunrmand.domain.model

data class Job(
    val id: String,
    val title: String,
    val description: String,
    val budget: Double,
    val creatorId: String,
    val date: Long = System.currentTimeMillis(),
    val location: String = "Lahore" // Default for now
)
