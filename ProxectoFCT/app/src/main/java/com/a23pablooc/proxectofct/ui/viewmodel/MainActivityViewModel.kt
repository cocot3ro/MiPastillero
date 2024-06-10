package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.usecases.BorrarTerminadosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val borrarTerminadosUseCase: BorrarTerminadosUseCase,
    private val userInfoProvider: UserInfoProvider
//    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) : ViewModel() {

    suspend fun checkFinished() {
        userInfoProvider.userChangedFlow.collect {
            borrarTerminadosUseCase.invoke()
        }
    }

    suspend fun checkNotifications() {
        userInfoProvider.userChangedFlow.collect {
            // TODO: Programar las siguientes notificaciones de los medicamentos
        }
    }
}