package com.llsit.joinsphere.feature.createevent.di

import com.llsit.joinsphere.feature.createevent.CreateEventViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createEventModule = module {
    viewModel { CreateEventViewModel(get()) }
}