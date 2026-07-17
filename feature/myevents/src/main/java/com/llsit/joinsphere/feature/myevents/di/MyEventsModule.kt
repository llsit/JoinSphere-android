package com.llsit.joinsphere.feature.myevents.di

import com.llsit.joinsphere.feature.myevents.MyEventsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val myEventsModule = module {
    viewModel { MyEventsViewModel(get(), get(), get()) }
}
