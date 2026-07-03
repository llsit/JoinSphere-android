package com.llsit.joinsphere.feature.notifications.di

import com.llsit.joinsphere.feature.notifications.NotificationsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val notificationsModule = module {
    viewModel { NotificationsViewModel() }
}
