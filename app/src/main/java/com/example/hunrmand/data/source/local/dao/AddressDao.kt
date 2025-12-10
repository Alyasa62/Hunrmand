package com.example.hunrmand.data.source.local.dao

import androidx.room.*
import com.example.hunrmand.data.source.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)

    @Query("SELECT * FROM addresses WHERE userId = :userId")
    fun getAddresses(userId: String): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    suspend fun getDefaultAddress(userId: String): AddressEntity?

    @Query("SELECT COUNT(*) FROM addresses WHERE userId = :userId")
    suspend fun getAddressCount(userId: String): Int

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun resetDefaultAddresses(userId: String)
}
