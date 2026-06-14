package com.llsit.joinsphere.core.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.data.repository.OfflineUserDataRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "joinsphere_preferences")

val dataModule = module {
    single { androidContext().dataStore }
    single { PreferencesDataSource(get()) }
    single<UserDataRepository> { OfflineUserDataRepository(get()) }
}
