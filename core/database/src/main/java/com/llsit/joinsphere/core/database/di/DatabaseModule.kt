package com.llsit.joinsphere.core.database.di

import androidx.room.Room
import com.llsit.joinsphere.core.database.JoinSphereDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            JoinSphereDatabase::class.java,
            "joinsphere-database"
        ).build()
    }
    single { get<JoinSphereDatabase>().categoryDao() }
}
