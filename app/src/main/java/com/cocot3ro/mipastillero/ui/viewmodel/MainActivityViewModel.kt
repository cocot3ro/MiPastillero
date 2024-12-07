package com.cocot3ro.mipastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cocot3ro.mipastillero.domain.usecases.ProgramarNotificacionesUseCase
import com.cocot3ro.mipastillero.domain.usecases.SetUpUserToLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase,
    private val setUpUserToLoginUseCase: SetUpUserToLoginUseCase
) : ViewModel() {

    suspend fun checkNotifications() {
        programarNotificacionesUseCase.invoke()
    }

    suspend fun setUpUserToLogin(userId: Long) {
        setUpUserToLoginUseCase.invoke(userId)
    }
}