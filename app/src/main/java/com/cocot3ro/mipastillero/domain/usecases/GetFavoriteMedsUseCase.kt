package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMedsUseCase @Inject constructor(private val repository: PillboxDbRepository) {

    fun invoke(): Flow<List<MedicamentoItem>> {
        return repository.getAllFavoriteMedsFlow()
    }

}
