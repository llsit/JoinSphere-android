package com.llsit.joinsphere.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            userDataRepository.setAuthToken(null)
            onComplete()
        }
    }
    
    fun login(onComplete: () -> Unit) {
        viewModelScope.launch {
            // Placeholder for login logic
            userDataRepository.setAuthToken("mock_token")
            onComplete()
        }
    }
}
