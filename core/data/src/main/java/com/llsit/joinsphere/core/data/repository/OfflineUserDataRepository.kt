package com.llsit.joinsphere.core.data.repository

import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow

class OfflineUserDataRepository(
    private val preferencesDataSource: PreferencesDataSource
) : UserDataRepository {
    override val shouldShowOnboarding: Flow<Boolean> = preferencesDataSource.shouldShowOnboarding
    
    override suspend fun setShouldShowOnboarding(shouldShow: Boolean) {
        preferencesDataSource.setShouldShowOnboarding(shouldShow)
    }

    override val authToken: Flow<String?> = preferencesDataSource.authToken

    override suspend fun setAuthToken(token: String?) {
        preferencesDataSource.setAuthToken(token)
    }
}
