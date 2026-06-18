package com.llsit.joinsphere.core.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.data.repository.AuthRepositoryImpl
import com.llsit.joinsphere.core.data.repository.OfflineUserDataRepository
import com.llsit.joinsphere.core.data.repository.ProfileRepositoryImpl
import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "joinsphere_preferences")

val dataModule = module {
    single { androidContext().dataStore }
    single { Firebase.firestore }
    single { Firebase.auth }
    single { Firebase.storage }
    single { PreferencesDataSource(get()) }
    single<UserDataRepository> { OfflineUserDataRepository(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get(), get()) }
}
