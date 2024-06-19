package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.usecases.AddMedicamentoUseCase
import com.a23pablooc.proxectofct.domain.usecases.ProgramarNotificacionesUseCase
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