package com.example.hunrmand.domain.model

data class User(
    val id: String,
    val username: String,
    val email: String,
    val role: UserRole
)
