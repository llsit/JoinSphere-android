package com.llsit.joinsphere.feature.discover.di

import com.llsit.joinsphere.feature.discover.DiscoverViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val discoverModule = module {
    viewModel { DiscoverViewModel() }
}