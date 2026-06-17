package com.llsit.joinsphere.feature.profile.state

import com.llsit.joinsphere.core.model.UserProfileDto

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfileDto) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}