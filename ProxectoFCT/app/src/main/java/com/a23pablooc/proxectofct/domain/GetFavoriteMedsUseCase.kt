package com.a23pablooc.proxectofct.domain

import com.a23pablooc.proxectofct.data.repositories.PillboxRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMedsUseCase @Inject constructor(private val repository: PillboxRepository) {

    operator fun invoke(): Flow<List<MedicamentoItem>> {
        return repository.getAllFavoriteMeds()
    }

}
