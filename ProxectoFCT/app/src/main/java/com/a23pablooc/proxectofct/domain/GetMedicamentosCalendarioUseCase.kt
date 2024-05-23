package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.PillboxRepository
import com.a23pablooc.proxectofct.data.database.extensions.toDomain
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetMedicamentosCalendarioUseCase @Inject constructor(private val repository: PillboxRepository) {
    operator fun invoke(date: Date): Flow<List<MedicamentoCalendarioItem>> {
        return repository.getAllWithMedicamentosByDiaOrderByHora(UserInfoProvider.id, date)
            .map { it -> it.map { it.toDomain() } }
    }
}