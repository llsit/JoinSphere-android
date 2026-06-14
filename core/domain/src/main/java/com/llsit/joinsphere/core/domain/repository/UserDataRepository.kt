package com.llsit.joinsphere.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val shouldShowOnboarding: Flow<Boolean>
    suspend fun setShouldShowOnboarding(shouldShow: Boolean)
    
    val authToken: Flow<String?>
    suspend fun setAuthToken(token: String?)
}
