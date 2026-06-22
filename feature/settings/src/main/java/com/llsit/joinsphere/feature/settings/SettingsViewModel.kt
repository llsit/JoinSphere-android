package com.llsit.joinsphere.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SettingsUiEffect {
    object LogoutSuccess : SettingsUiEffect
}

class SettingsViewModel(
    private val userDataRepository: UserDataRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<SettingsUiEffect>()
    val effect: SharedFlow<SettingsUiEffect> = _effect.asSharedFlow()

    val shouldShowOnboarding: StateFlow<Boolean> = userDataRepository.shouldShowOnboarding
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun resetOnboarding() {
        viewModelScope.launch {
            userDataRepository.setShouldShowOnboarding(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                _effect.emit(SettingsUiEffect.LogoutSuccess)
            }
        }
    }
}
