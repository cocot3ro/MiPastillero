package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import javax.inject.Inject

class AddMedicamentoActivoUseCase @Inject constructor(private val repository: PillboxDbRepository) {
    suspend operator fun invoke(med: MedicamentoActivoItem) {
        repository.addMedicamentoActivo(med)
    }
}