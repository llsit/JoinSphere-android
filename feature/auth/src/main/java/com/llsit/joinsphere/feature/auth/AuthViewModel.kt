package com.llsit.joinsphere.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)


sealed interface AuthIntent {
    object Login : AuthIntent
    object Register : AuthIntent
    object Logout : AuthIntent
    object ClearError : AuthIntent
}
class AuthViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun processIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Login -> login()
            is AuthIntent.Logout -> logout()
            AuthIntent.Register -> register()
            is AuthIntent.ClearError -> _uiState.update { it.copy(errorMessage = null) }

        }
    }

    private fun register() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }


        }
    }
    
    private fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                userDataRepository.setAuthToken("mock_token")
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Login Failed") }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userDataRepository.setAuthToken(null)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isAuthenticated = false
                )
            }
        }
    }
}
