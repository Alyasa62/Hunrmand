package com.example.hunrmand.data.repository

import com.example.hunrmand.data.source.local.LocalAuthDataSource
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.data.source.local.entity.UserEntity
import com.example.hunrmand.data.util.SecurityUtils
import com.example.hunrmand.domain.model.User
import com.example.hunrmand.domain.model.UserRole
import com.example.hunrmand.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.util.UUID

class AuthRepositoryImpl(
    private val localAuthDataSource: LocalAuthDataSource,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun register(user: User, password: String): Result<Unit> {
        return try {
            val existingUser = localAuthDataSource.getUserByEmail(user.email)
            if (existingUser != null) {
                return Result.failure(Exception("User already exists"))
            }

            val passwordHash = SecurityUtils.hashPassword(password)
            val userEntity = UserEntity(
                id = user.id,
                username = user.username,
                email = user.email,
                passwordHash = passwordHash,
                role = user.role.name
                // For WORKER, additional fields would be updated separately or in a specific flow.
                // Assuming basic registration here.
            )
            localAuthDataSource.registerUser(userEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val userEntity = localAuthDataSource.getUserByEmail(email)
                ?: return Result.failure(Exception("User not found"))

            val passwordHash = SecurityUtils.hashPassword(password)
            if (userEntity.passwordHash != passwordHash) {
                return Result.failure(Exception("Invalid password"))
            }

            val user = User(
                id = userEntity.id,
                username = userEntity.username,
                email = userEntity.email,
                role = UserRole.valueOf(userEntity.role)
            )

            // Save session
            sessionManager.saveUserSession(user.id, user.role.name)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }

    override fun getCurrentUser(): Flow<User?> {
        return combine(sessionManager.userId, sessionManager.userRole) { userId, roleStr ->
            if (userId != null && roleStr != null) {
                val userEntity = localAuthDataSource.getUserById(userId)
                if (userEntity != null) {
                    User(
                        id = userEntity.id,
                        username = userEntity.username,
                        email = userEntity.email,
                        role = UserRole.valueOf(userEntity.role)
                    )
                } else null
            } else null
        }
    }
}
