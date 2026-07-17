package com.llsit.joinsphere.feature.eventdetail.di

import com.llsit.joinsphere.feature.eventdetail.EventDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val eventDetailModule = module {
    viewModel { EventDetailViewModel(get(), get(), get(), get()) }
}
