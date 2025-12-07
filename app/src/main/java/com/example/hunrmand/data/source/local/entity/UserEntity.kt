package com.example.hunrmand.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val email: String,
    val passwordHash: String, // Store Hashed Password
    val role: String, // Store UserRole as String
    val profession: String? = null,
    val rating: Double = 0.0,
    val hourlyRate: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
