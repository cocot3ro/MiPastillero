package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetMedicamentosCalendarioUseCase @Inject constructor(private val repository: MiPastilleroDbRepository) {
    fun invoke(date: Date): Flow<List<MedicamentoActivoItem>> {
        return repository.getMedicamentosActivosFlow().map { list ->
            list.filter { it.fechaInicio <= date && it.fechaFin >= date }
        }
    }
}