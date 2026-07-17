package com.llsit.joinsphere.feature.discover.di

import com.llsit.joinsphere.core.domain.usecase.GetCurrentLocationUseCase
import com.llsit.joinsphere.core.domain.usecase.GetDiscoverFeedsUseCase
import com.llsit.joinsphere.feature.discover.DiscoverViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val discoverModule = module {
    factoryOf(::GetCurrentLocationUseCase)
    factoryOf(::GetDiscoverFeedsUseCase)
    viewModel { DiscoverViewModel(get(), get(), get(), get()) }
}
