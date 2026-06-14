package com.llsit.joinsphere.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            userDataRepository.setShouldShowOnboarding(false)
        }
    }
}
