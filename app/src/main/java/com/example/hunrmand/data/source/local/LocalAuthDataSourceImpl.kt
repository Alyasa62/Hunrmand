package com.example.hunrmand.data.source.local

import com.example.hunrmand.data.source.local.dao.UserDao
import com.example.hunrmand.data.source.local.entity.UserEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalAuthDataSourceImpl(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalAuthDataSource {

    override suspend fun registerUser(user: UserEntity) = withContext(ioDispatcher) {
        userDao.insertUser(user)
    }

    override suspend fun getUserByEmail(email: String): UserEntity? = withContext(ioDispatcher) {
        userDao.getUserByEmail(email)
    }

    override suspend fun getUserById(userId: String): UserEntity? = withContext(ioDispatcher) {
        userDao.getUserById(userId)
    }

    override suspend fun getUsersByRole(role: String): List<UserEntity> = withContext(ioDispatcher) {
        userDao.getUsersByRole(role)
    }

    override suspend fun deleteUser(userId: String) = withContext(ioDispatcher) {
        userDao.deleteUser(userId)
    }
}
