package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMedsUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    operator fun invoke(): Flow<List<MedicamentoItem>> {
        return repository.getAllFavoriteMeds()
    }

}
