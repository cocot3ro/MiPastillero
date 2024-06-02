package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import javax.inject.Inject

class AddMedicamentoActivoUseCase @Inject constructor(private val repository: PillboxRepository) {
    suspend operator fun invoke(med: MedicamentoActivoItem) {
        repository.addMedicamentoActivo(med)
    }
}