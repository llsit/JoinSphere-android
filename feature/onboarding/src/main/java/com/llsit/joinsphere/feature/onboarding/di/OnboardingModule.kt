package com.llsit.joinsphere.feature.onboarding.di

import com.llsit.joinsphere.feature.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onboardingModule = module {
    viewModel { OnboardingViewModel(get()) }
}
