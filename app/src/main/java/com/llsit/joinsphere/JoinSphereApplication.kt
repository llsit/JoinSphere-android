package com.llsit.joinsphere

import android.app.Application
import com.llsit.joinsphere.feature.auth.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class JoinSphereApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@JoinSphereApplication)
            modules(
                authModule,
                // Add other feature modules here as they are created
            )
        }
    }
}
