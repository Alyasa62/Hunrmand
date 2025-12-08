package com.example.hunrmand.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val label: String, // e.g. "Home", "Work"
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double,
    val isDefault: Boolean = false
)
