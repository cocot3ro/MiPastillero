package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoActivoDAO
import com.a23pablooc.proxectofct.data.database.dao.MedicamentoDAO
import com.a23pablooc.proxectofct.data.database.dao.UsuarioDAO
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.model.extensions.toDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    /*private val checkFinishedUseCase: CheckFinishedUseCase*/
    private val usuarioDAO: UsuarioDAO,
    private val medicamentoDAO: MedicamentoDAO,
    private val medicamentoActivoDAO: MedicamentoActivoDAO
) : ViewModel() {
    // TODO: Implement the ViewModel and inject the use cases

    fun checkFinished() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: checkFinishedUseCase()
        }
    }

    @Deprecated("Only for testing purposes. Remove this method when not needed anymore.")
    fun example(currentUser: UsuarioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            usuarioDAO.insert(currentUser.toDatabase())
        }
    }
}