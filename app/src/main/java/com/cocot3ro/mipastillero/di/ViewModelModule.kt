package com.cocot3ro.mipastillero.di

import com.cocot3ro.mipastillero.ui.screens.splash.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::SplashViewModel)

}
