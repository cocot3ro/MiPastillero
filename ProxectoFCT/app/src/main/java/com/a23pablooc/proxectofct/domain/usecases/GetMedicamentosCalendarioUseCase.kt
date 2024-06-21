package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetMedicamentosCalendarioUseCase @Inject constructor(private val repository: PillboxDbRepository) {
    fun invoke(date: Date): Flow<List<MedicamentoActivoItem>> {
        return repository.getMedicamentosActivosFlow().map { list ->
            list.filter { it.fechaInicio <= date && it.fechaFin >= date }
        }
    }
}