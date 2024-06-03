package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date
import javax.inject.Inject

class MarcarTomaUseCase @Inject constructor(private val repository: PillboxDbRepository) {
    suspend operator fun invoke(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        if (med.tomas[dia] != null) {
            if (med.tomas[dia]!![hora] != null) {
                med.tomas[dia]!![hora] = !med.tomas[dia]!![hora]!!
            } else {
                med.tomas[dia]!![hora] = true
            }
        } else {
            med.tomas[dia] = mutableMapOf(hora to true)
        }

        repository.update(med)
    }

}
