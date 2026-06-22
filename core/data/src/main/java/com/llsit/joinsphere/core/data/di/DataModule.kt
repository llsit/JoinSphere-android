package com.llsit.joinsphere.core.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.LocationServices
import com.llsit.joinsphere.core.data.local.PreferencesDataSource
import com.llsit.joinsphere.core.data.local.SupabaseSessionManager
import com.llsit.joinsphere.core.data.repository.AuthRepositoryImpl
import com.llsit.joinsphere.core.data.repository.EventRepositoryImpl
import com.llsit.joinsphere.core.data.repository.LocationRepositoryImpl
import com.llsit.joinsphere.core.data.repository.OfflineUserDataRepository
import com.llsit.joinsphere.core.data.repository.ProfileRepositoryImpl
import com.llsit.joinsphere.core.domain.repository.AuthRepository
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.LocationRepository
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "joinsphere_preferences")

val dataModule = module {
    single { androidContext().dataStore }
    single {
        createSupabaseClient(
            supabaseUrl = "https://zafcvisvvwsxxefvduja.supabase.co",
            supabaseKey = "sb_publishable_9suESlI7TT5YFSDGHs0N6Q_uPDi6jCz"
        ) {
            install(Auth) {
                sessionManager = get<SessionManager>()
            }
            install(Functions)
            install(Postgrest)
            install(Storage)
        }
    }
    single { PreferencesDataSource(get()) }
    single<SessionManager> { SupabaseSessionManager(get()) }
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single<LocationRepository> { LocationRepositoryImpl(androidContext(), get()) }
    single<UserDataRepository> { OfflineUserDataRepository(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<EventRepository> { EventRepositoryImpl(get()) }
}
