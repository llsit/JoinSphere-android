package com.llsit.joinsphere.feature.createevent.di

import com.llsit.joinsphere.feature.createevent.CreateEventViewModel
import com.llsit.joinsphere.feature.createevent.LocationPickerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val createEventModule = module {
    viewModel { CreateEventViewModel(get()) }
    viewModel { LocationPickerViewModel(get(), get()) }
}