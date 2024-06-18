package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.domain.usecases.ProgramarNotificacionesUseCase
import com.a23pablooc.proxectofct.domain.usecases.SetUpUserToLoginUseCase
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