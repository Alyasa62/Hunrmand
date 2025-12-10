package com.example.hunrmand.presentation.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.data.source.local.entity.AddressEntity
import com.example.hunrmand.domain.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first

class AddressViewModel(
    private val addressRepository: AddressRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val addresses: StateFlow<List<AddressEntity>> = sessionManager.userId
        .flatMapLatest { userId ->
            if (userId != null) addressRepository.getAddresses(userId) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow<AddressUiState>(AddressUiState.Loading)
    val uiState: StateFlow<AddressUiState> = _uiState.asStateFlow()

    fun addAddress(label: String, fullAddress: String, lat: Double, lng: Double, isDefault: Boolean) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            if (isDefault) {
                addressRepository.resetDefaultAddresses(userId)
            }
            val newAddress = AddressEntity(
                userId = userId,
                label = label,
                fullAddress = fullAddress,
                latitude = lat,
                longitude = lng,
                isDefault = isDefault
            )
            addressRepository.insertAddress(newAddress)
        }
    }

    fun deleteAddress(address: AddressEntity) {
        viewModelScope.launch {
            addressRepository.deleteAddress(address)
        }
    }

    fun updateAddress(address: AddressEntity) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            if (address.isDefault) {
                addressRepository.resetDefaultAddresses(userId)
            }
            addressRepository.updateAddress(address)
        }
    }

    fun setDefault(address: AddressEntity) {
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            addressRepository.resetDefaultAddresses(userId)
            addressRepository.updateAddress(address.copy(isDefault = true))
        }
    }
}

sealed class AddressUiState {
    object Loading : AddressUiState()
    object Success : AddressUiState()
    data class Error(val message: String) : AddressUiState()
}
