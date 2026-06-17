package com.llsit.joinsphere.feature.profile

import androidx.lifecycle.ViewModel
import com.llsit.joinsphere.feature.profile.state.ProfileIntent
import com.llsit.joinsphere.feature.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {

    }

    fun processIntent(intent: ProfileIntent) {

    }
}