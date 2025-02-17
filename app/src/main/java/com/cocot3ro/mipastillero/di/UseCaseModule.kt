package com.cocot3ro.mipastillero.di

import com.cocot3ro.mipastillero.domain.usecases.LogInUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::LogInUseCase)

}