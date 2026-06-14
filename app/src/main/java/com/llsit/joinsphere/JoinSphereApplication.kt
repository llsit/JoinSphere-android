package com.llsit.joinsphere

import android.app.Application
import com.llsit.joinsphere.core.data.di.dataModule
import com.llsit.joinsphere.di.appModule
import com.llsit.joinsphere.feature.auth.di.authModule
import com.llsit.joinsphere.feature.onboarding.di.onboardingModule
import com.llsit.joinsphere.feature.settings.di.settingsModule
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
                appModule,
                dataModule,
                authModule,
                onboardingModule,
                settingsModule,
            )
        }
    }
}
