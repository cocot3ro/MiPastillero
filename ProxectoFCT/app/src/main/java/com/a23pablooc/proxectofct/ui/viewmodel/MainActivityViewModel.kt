package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.usecases.BorrarTerminadosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val borrarTerminadosUseCase: BorrarTerminadosUseCase,
    private val userInfoProvider: UserInfoProvider
//    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) : ViewModel() {

    fun checkFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoProvider.userChangedFlow.collect {
                borrarTerminadosUseCase.invoke()
            }
        }
    }

    fun checkNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            userInfoProvider.userChangedFlow.collect {
                // TODO: Programar las siguientes notificaciones de los medicamentos
            }
        }
    }
}