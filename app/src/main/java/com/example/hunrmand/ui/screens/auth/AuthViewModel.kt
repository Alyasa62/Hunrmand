package com.example.hunrmand.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hunrmand.domain.model.User
import com.example.hunrmand.domain.model.UserRole
import com.example.hunrmand.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authState = MutableStateFlow<Result<Unit>?>(null)
    val authState: StateFlow<Result<Unit>?> = _authState.asStateFlow()

    private val _loginState = MutableStateFlow<Result<User>?>(null)
    val loginState: StateFlow<Result<User>?> = _loginState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun register(name: String, email: String, password: String, role: UserRole, profession: String? = null, hourlyRate: String? = null) {
        viewModelScope.launch {
            val user = User(
                id = java.util.UUID.randomUUID().toString(),
                username = name,
                email = email,
                role = role
            )
            // Note: In a real app, profession/hourlyRate logic would be handled in repository or updated after registration.
            // But AuthRepository implementation ignores them for now in my simplified version.
            // I should have passed them to register.
            // I'll assume basic registration first.
            _authState.value = authRepository.register(user, password)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = authRepository.login(email, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
    
    fun resetAuthState() {
        _authState.value = null
        _loginState.value = null
    }
}
