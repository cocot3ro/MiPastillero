package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import com.a23pablooc.proxectofct.domain.usecases.BorrarTerminadosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val borrarTerminadosUseCase: BorrarTerminadosUseCase,
    private val usuarioDAO: UsuarioDAO, // TODO: Remove this line when not needed anymore.
) : ViewModel() {

    fun checkFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            borrarTerminadosUseCase.invoke()
        }
    }

    @Deprecated("Only for testing purposes. Remove this method when not needed anymore.")
    fun insertTestUser(user: UsuarioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            usuarioDAO.insert(user.toDatabase())
        }
    }
}