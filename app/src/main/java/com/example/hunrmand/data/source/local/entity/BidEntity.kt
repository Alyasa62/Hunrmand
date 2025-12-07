package com.example.hunrmand.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bids")
data class BidEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val workerId: String,
    val amount: Double,
    val estimatedTime: String, // e.g. "2 hours" or "2 days"
    val status: String, // PENDING, ACCEPTED, REJECTED
    val createdAt: Long = System.currentTimeMillis()
)
