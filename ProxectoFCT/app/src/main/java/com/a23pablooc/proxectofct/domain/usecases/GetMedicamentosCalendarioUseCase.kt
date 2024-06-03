package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetMedicamentosCalendarioUseCase @Inject constructor(private val repository: PillboxDbRepository) {
    operator fun invoke(date: Date): Flow<List<MedicamentoActivoItem>> {
        return repository.getAllWithMedicamentosByDiaOrderByHora(date)
    }
}