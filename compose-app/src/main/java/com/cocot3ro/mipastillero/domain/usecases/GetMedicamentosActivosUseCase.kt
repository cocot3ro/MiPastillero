package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicamentosActivosUseCase @Inject constructor(private val repository: MiPastilleroDbRepository) {

    fun invoke(): Flow<List<MedicamentoActivoItem>> {
        val today = DateTimeUtils.now
        return repository.getMedicamentosActivosFlow().map { list ->
            list.filter { it.fechaFin >= today }
        }
    }
}