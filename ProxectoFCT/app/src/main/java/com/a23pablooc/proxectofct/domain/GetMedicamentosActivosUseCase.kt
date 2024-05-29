package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetMedicamentosActivosUseCase @Inject constructor(private val repository: PillboxRepository) {

    operator fun invoke(): Flow<List<MedicamentoActivoItem>> {
        val fromDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        return repository.getMedicamentosActivos(fromDate)
    }

}
