package com.a23pablooc.proxectofct.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.usecases.UpdateMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditActiveMedViewModel @Inject constructor(
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase
) : ViewModel() {

    fun saveChanges(originalMed: MedicamentoActivoItem, med: MedicamentoActivoItem) {
        med.tomas =
            med.tomas.filterNot { (date, _) -> date < med.fechaInicio || date > med.fechaFin }
                .toMutableMap()

        val horas = originalMed.horario.map {
            Calendar.getInstance().apply {
                time = it
                set(0, 0, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
        }.toSet()

        med.tomas = med.tomas.filter { (date, _) ->
            Calendar.getInstance().apply {
                time = date
                set(0, 0, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time in horas
        }.toMutableMap()

        viewModelScope.launch(Dispatchers.IO) {
            updateMedicamentoUseCase.invoke(med)
        }
    }
}