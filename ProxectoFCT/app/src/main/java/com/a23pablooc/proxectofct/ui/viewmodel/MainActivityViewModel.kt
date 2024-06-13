package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.core.UserInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userInfoProvider: UserInfoProvider
//    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) : ViewModel() {

    suspend fun checkNotifications() {
        userInfoProvider.userChangedFlow.collect {
            // TODO: Programar las siguientes notificaciones de los medicamentos
        }
    }
}