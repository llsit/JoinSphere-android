package com.llsit.joinsphere.core.data.repository

import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.Flow

class OfflineUserDataRepository(
    private val preferencesDataSource: PreferencesDataSource,
    private val supabase: SupabaseClient
) : UserDataRepository {
    override val shouldShowOnboarding: Flow<Boolean> = preferencesDataSource.shouldShowOnboarding
    
    override suspend fun setShouldShowOnboarding(shouldShow: Boolean) {
        preferencesDataSource.setShouldShowOnboarding(shouldShow)
    }

    override val currentUserId: String?
        get() = supabase.auth.currentUserOrNull()?.id
}
