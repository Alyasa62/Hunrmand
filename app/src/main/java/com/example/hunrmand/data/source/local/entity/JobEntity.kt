package com.example.hunrmand.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val budget: Double,
    val creatorId: String,
    val createdAt: Long,
    val userId: String, // Added to match repository logic
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null
)
