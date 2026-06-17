package com.llsit.joinsphere.feature.profile.di

import com.llsit.joinsphere.feature.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    viewModel { ProfileViewModel() }
}
