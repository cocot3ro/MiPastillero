package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import javax.inject.Inject

class UpdateMedicamentoUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {

    suspend fun invoke(medicamento: MedicamentoItem) {
        pillboxDbRepository.updateMedicamento(medicamento)
    }

}