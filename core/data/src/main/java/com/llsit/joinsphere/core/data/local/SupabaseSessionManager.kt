package com.llsit.joinsphere.core.data.local

import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

class SupabaseSessionManager(
    private val preferencesDataSource: PreferencesDataSource
) : SessionManager {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun saveSession(session: UserSession) {
        try {
            val sessionString = json.encodeToString(session)
            preferencesDataSource.setSessionData(sessionString)
        } catch (e: Exception) {
            Timber.e(e, "Failed to save session")
        }
    }

    override suspend fun loadSession(): UserSession {
        // This method is expected to throw if no session is found in some versions of Supabase-kt
        val sessionString = preferencesDataSource.sessionDataValue() ?: error("No session stored")
        return json.decodeFromString(sessionString)
    }

    override suspend fun loadSessionOrNull(): UserSession? {
        return try {
            val sessionString = preferencesDataSource.sessionDataValue() ?: return null
            json.decodeFromString(sessionString)
        } catch (e: Exception) {
            Timber.e(e, "Failed to load session")
            null
        }
    }

    override suspend fun deleteSession() {
        preferencesDataSource.setSessionData(null)
    }
}
