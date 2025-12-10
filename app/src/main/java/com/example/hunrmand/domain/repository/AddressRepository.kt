package com.example.hunrmand.domain.repository

import com.example.hunrmand.data.source.local.dao.AddressDao
import com.example.hunrmand.data.source.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

class AddressRepository(private val addressDao: AddressDao) {
    fun getAddresses(userId: String): Flow<List<AddressEntity>> = addressDao.getAddresses(userId)

    suspend fun insertAddress(address: AddressEntity) = addressDao.insertAddress(address)

    suspend fun updateAddress(address: AddressEntity) = addressDao.updateAddress(address)

    suspend fun deleteAddress(address: AddressEntity) = addressDao.deleteAddress(address)

    suspend fun getDefaultAddress(userId: String): AddressEntity? = addressDao.getDefaultAddress(userId)

    suspend fun getAddressCount(userId: String): Int = addressDao.getAddressCount(userId)

    suspend fun resetDefaultAddresses(userId: String) = addressDao.resetDefaultAddresses(userId)
}
