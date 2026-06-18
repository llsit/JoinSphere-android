package com.llsit.joinsphere.feature.profile.state

import com.llsit.joinsphere.core.model.UserProfileDto

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfileDto? = null,
    val errorMessage: String? = null
)