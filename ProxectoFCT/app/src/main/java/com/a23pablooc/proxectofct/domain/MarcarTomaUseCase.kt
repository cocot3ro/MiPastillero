package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import javax.inject.Inject

class MarcarTomaUseCase @Inject constructor(private val repository: PillboxRepository) {
    suspend operator fun invoke(med: MedicamentoCalendarioItem) {
        med.seHaTomado = med.seHaTomado.not()
        repository.updateMedicamentoCalendario(med)
    }

}
