package com.llsit.joinsphere.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.domain.usecase.LoginUseCase
import com.llsit.joinsphere.core.domain.usecase.LogoutUseCase
import com.llsit.joinsphere.core.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDataRepository: UserDataRepository,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<AuthUiEffect>()
    val effect: SharedFlow<AuthUiEffect> = _effect.asSharedFlow()

    fun processIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Login -> login(intent.email, intent.password)
            is AuthIntent.Logout -> logout()
            is AuthIntent.Register -> register(intent.name, intent.email, intent.password)
            is AuthIntent.ClearError -> _uiState.update { it.copy(errorMessage = null) }
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            registerUseCase.invoke(name, email, password).onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false) }
                val errorMsg = e.localizedMessage ?: "Register Failed"
                _effect.emit(AuthUiEffect.ShowToast(errorMsg))
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            loginUseCase(email, password).onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false) }
                val errorMsg = e.localizedMessage ?: "Login Failed"
                _effect.emit(AuthUiEffect.ShowToast(errorMsg))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            logoutUseCase().onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = false
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false) }
                _effect.emit(AuthUiEffect.ShowToast(e.localizedMessage ?: "Logout Failed"))
            }
        }
    }
}
