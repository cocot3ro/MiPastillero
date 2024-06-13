package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicamentosActivosUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    fun invoke(): Flow<List<MedicamentoActivoItem>> {
        val today = DateTimeUtils.today.zeroTime()
        return repository.getMedicamentosActivos().map { list ->
            list.filter { it.fechaFin >= today }
        }
    }
}