package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import java.util.Date
import javax.inject.Inject

class GetMedicamentosActivosUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    operator fun invoke(): Flow<List<MedicamentoActivoItem>> {
        return repository.getMedicamentosActivos(Date().zeroTime())
    }

}
