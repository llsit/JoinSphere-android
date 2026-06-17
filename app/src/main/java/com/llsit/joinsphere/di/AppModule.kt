package com.llsit.joinsphere.di

import com.llsit.joinsphere.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SplashViewModel(get()) }
}