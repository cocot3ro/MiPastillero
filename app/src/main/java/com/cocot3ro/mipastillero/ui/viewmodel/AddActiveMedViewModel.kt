package com.cocot3ro.mipastillero.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.cocot3ro.mipastillero.data.network.CimaApiDefinitions
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.domain.usecases.AddMedicamentoUseCase
import com.cocot3ro.mipastillero.domain.usecases.ProgramarNotificacionesUseCase
import com.cocot3ro.mipastillero.domain.usecases.SearchMedicamentoUseCase
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