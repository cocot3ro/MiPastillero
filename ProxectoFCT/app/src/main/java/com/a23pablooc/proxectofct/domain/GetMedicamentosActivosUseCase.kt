package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import java.util.Date
import javax.inject.Inject

class GetMedicamentosActivosUseCase @Inject constructor(private val repository: PillboxRepository) {

    operator fun invoke(): Flow<List<MedicamentoActivoItem>> {
        return repository.getMedicamentosActivos(Date().zeroTime())
    }

}
