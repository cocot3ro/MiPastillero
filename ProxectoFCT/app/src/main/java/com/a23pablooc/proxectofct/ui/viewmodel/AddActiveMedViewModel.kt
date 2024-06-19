package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.AddMedicamentoUseCase
import com.a23pablooc.proxectofct.domain.usecases.ProgramarNotificacionesUseCase
import com.a23pablooc.proxectofct.domain.usecases.SearchMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddActiveMedViewModel @Inject constructor(
    private val addMedicamentoUseCase: AddMedicamentoUseCase,
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase,
    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase
) : ViewModel() {
    suspend fun search(codNacional: String): MedicamentoItem? {
        if (!CimaApiDefinitions.codNacionalPattern.matches(codNacional))
            throw IllegalArgumentException("Invalid codNacional")

        return searchMedicamentoUseCase.invoke(codNacional.substringBefore('.').toLong())
    }

    suspend fun onDataEntered(med: MedicamentoActivoItem) {
        addMedicamentoUseCase.invoke(med)
        programarNotificacionesUseCase.invoke()
    }
}