package com.llsit.joinsphere.feature.search.di

import com.llsit.joinsphere.feature.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { SearchViewModel(get()) }
}
