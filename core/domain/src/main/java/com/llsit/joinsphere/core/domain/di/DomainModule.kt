package com.llsit.joinsphere.core.domain.di

import com.llsit.joinsphere.core.domain.usecase.LoginUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::LoginUseCase)
}
