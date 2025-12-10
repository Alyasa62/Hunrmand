package com.example.hunrmand.data.source.local

import com.example.hunrmand.data.source.local.entity.UserEntity

interface LocalAuthDataSource {
    suspend fun registerUser(user: UserEntity)
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun getUserById(userId: String): UserEntity?
    suspend fun getUsersByRole(role: String): List<UserEntity>
    suspend fun deleteUser(userId: String)
}
