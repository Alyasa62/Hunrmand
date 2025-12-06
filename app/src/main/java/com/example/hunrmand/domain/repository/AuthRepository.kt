package com.example.hunrmand.domain.repository

import com.example.hunrmand.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(user: User, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logout()
    fun getCurrentUser(): Flow<User?>
}
