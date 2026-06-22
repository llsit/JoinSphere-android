package com.llsit.joinsphere.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesDataSource(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val SHOULD_SHOW_ONBOARDING = booleanPreferencesKey("should_show_onboarding")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val SESSION_DATA = stringPreferencesKey("session_data")
    }

    val shouldShowOnboarding: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.SHOULD_SHOW_ONBOARDING] ?: true
        }

    suspend fun setShouldShowOnboarding(shouldShow: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOULD_SHOW_ONBOARDING] = shouldShow
        }
    }

    val authToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN]
        }

    suspend fun authTokenValue(): String? = authToken.firstOrNull()

    suspend fun setAuthToken(token: String?) {
        dataStore.edit { preferences ->
            if (token == null) {
                preferences.minusAssign(PreferencesKeys.AUTH_TOKEN)
            } else {
                preferences[PreferencesKeys.AUTH_TOKEN] = token
            }
        }
    }

    val sessionData: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.SESSION_DATA]
        }

    suspend fun sessionDataValue(): String? = sessionData.firstOrNull()

    suspend fun setSessionData(session: String?) {
        dataStore.edit { preferences ->
            if (session == null) {
                preferences.minusAssign(PreferencesKeys.SESSION_DATA)
            } else {
                preferences[PreferencesKeys.SESSION_DATA] = session
            }
        }
    }
}
