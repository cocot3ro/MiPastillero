package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMedicamentosTerminadosUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {
    fun invoke(): Flow<List<MedicamentoActivoItem>> {
        return pillboxDbRepository.getMedicamentosActivosFlow().map { list ->
            list.filter { it.fechaFin < DateTimeUtils.now }
        }
    }
}