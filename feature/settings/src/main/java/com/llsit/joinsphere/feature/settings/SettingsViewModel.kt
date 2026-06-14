package com.llsit.joinsphere.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

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
}
