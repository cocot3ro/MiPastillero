package com.cocot3ro.mipastillero.ui.screens.splash

sealed class SplashUiState {

    data object Loading : SplashUiState()
    data class Success<T>(val data: T) : SplashUiState()

}
