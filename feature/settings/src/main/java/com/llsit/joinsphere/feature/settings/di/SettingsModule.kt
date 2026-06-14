package com.llsit.joinsphere.feature.settings.di

import com.llsit.joinsphere.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { SettingsViewModel(get()) }
}
