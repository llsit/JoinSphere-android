package com.llsit.joinsphere.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.domain.repository.EventRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashUiState(
    val isLoading: Boolean = true
)

sealed interface SplashUiEffect {
    data class NavigateTo(val destination: SplashDestination) : SplashUiEffect
}

enum class SplashDestination {
    ONBOARDING,
    LOGIN,
    HOME
}

class SplashViewModel(
    private val preferencesDataSource: PreferencesDataSource,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<SplashUiEffect>()
    val effect: SharedFlow<SplashUiEffect> = _effect.asSharedFlow()

    init {
        checkNavigationDestination()
    }

    fun checkNavigationDestination() {
        viewModelScope.launch {
            // Start syncing categories in parallel
            val syncJob = async { eventRepository.syncCategories() }
            
            val shouldShowOnboarding = preferencesDataSource.shouldShowOnboarding.first()
            val token = preferencesDataSource.sessionDataValue()

            // Wait for sync to complete (optional, but good for first run)
            syncJob.await()

            val destination = when {
                shouldShowOnboarding -> SplashDestination.ONBOARDING
                token.isNullOrBlank() -> SplashDestination.LOGIN
                else -> SplashDestination.HOME
            }

            _uiState.update { it.copy(isLoading = false) }
            _effect.emit(SplashUiEffect.NavigateTo(destination))
        }
    }
}
