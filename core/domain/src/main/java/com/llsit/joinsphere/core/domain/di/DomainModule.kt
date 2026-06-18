package com.llsit.joinsphere.core.domain.di

import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.domain.usecase.CreateEventUseCase
import com.llsit.joinsphere.core.domain.usecase.LoginUseCase
import com.llsit.joinsphere.core.domain.usecase.RegisterUseCase
import com.llsit.joinsphere.core.domain.usecase.UploadImageProfileUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::UploadImageProfileUseCase)
    factory { CreateEventUseCase(get<EventRepository>(), get<UserDataRepository>(), androidContext()) }
}
