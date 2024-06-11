package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicamentosActivosUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    fun invoke(): Flow<List<MedicamentoActivoItem>> {
        return repository.getMedicamentosActivos()
    }

}
