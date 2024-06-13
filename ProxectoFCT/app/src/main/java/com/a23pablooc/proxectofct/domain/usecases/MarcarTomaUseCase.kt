package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date
import javax.inject.Inject

class MarcarTomaUseCase @Inject constructor(private val repository: PillboxDbRepository) {
    suspend fun invoke(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        med.tomas[dia]?.let { dayMap ->
            dayMap[hora] = dayMap[hora]?.not() ?: true
        } ?: run {
            med.tomas[dia] = mutableMapOf(hora to true)
        }
        repository.updateMed(med)
    }
}