package com.llsit.joinsphere.feature.auth.di

import com.llsit.joinsphere.feature.auth.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel { AuthViewModel(get()) }
}
