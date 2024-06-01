package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetMedicamentosCalendarioUseCase @Inject constructor(private val repository: PillboxRepository) {
    operator fun invoke(date: Date): Flow<List<MedicamentoActivoItem>> {
        return repository.getAllWithMedicamentosByDiaOrderByHora(date)
    }
}