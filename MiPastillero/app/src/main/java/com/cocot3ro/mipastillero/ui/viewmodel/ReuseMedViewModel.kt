package com.cocot3ro.mipastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.usecases.AddMedicamentoUseCase
import com.cocot3ro.mipastillero.domain.usecases.ProgramarNotificacionesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReuseMedViewModel @Inject constructor(
    private val addMedicamentoUseCase: AddMedicamentoUseCase,
    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) : ViewModel() {

    fun onDataEntered(med: MedicamentoActivoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addMedicamentoUseCase.invoke(med)
            programarNotificacionesUseCase.invoke()
        }
    }
}