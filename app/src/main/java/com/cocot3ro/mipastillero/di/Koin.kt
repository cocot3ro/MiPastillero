package com.cocot3ro.mipastillero.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(koinConfiguration: KoinAppDeclaration?) {
    startKoin {
        koinConfiguration?.invoke(this)
        modules(
            roomModule,
            ktorModule,
            useCaseModule,
            userInfoModule,
            viewModelModule,
            preferencesModule
        )
    }
}
