package com.cocot3ro.mipastillero.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.mipastillero.domain.usecases.LogInUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val logInUseCase: LogInUseCase
) : ViewModel() {

    private val _firstTime: MutableStateFlow<SplashUiState> =
        MutableStateFlow(SplashUiState.Loading)
    val firstTime: StateFlow<SplashUiState> = _firstTime

    private val _defaultUser: MutableStateFlow<SplashUiState> =
        MutableStateFlow(SplashUiState.Loading)
    val defaultUser: StateFlow<SplashUiState> = _defaultUser

    private val _usersFlow: MutableStateFlow<SplashUiState> =
        MutableStateFlow(SplashUiState.Loading)
    val usersFlow: StateFlow<SplashUiState> = _usersFlow

    fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {

        }

        viewModelScope.launch(Dispatchers.IO) {

        }

        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun login(user: Long) = logInUseCase(user)

}
