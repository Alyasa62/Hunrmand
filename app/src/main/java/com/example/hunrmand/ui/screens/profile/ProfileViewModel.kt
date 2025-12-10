package com.example.hunrmand.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.data.source.local.dao.UserDao
import com.example.hunrmand.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

import com.example.hunrmand.domain.repository.AddressRepository

class ProfileViewModel(
    private val userDao: UserDao,
    private val sessionManager: SessionManager,
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user.asStateFlow()

    private val _addressCount = MutableStateFlow(0)
    val addressCount: StateFlow<Int> = _addressCount.asStateFlow()

    init {
        fetchUserProfile()
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            val userId = sessionManager.userId.first()
            if (userId != null) {
                _user.value = userDao.getUserById(userId)
                _addressCount.value = addressRepository.getAddressCount(userId)
            }
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            userDao.updateUser(user)
            _user.value = user
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLogoutComplete()
        }
    }
}
