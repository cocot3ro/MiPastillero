package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMedsUseCase @Inject constructor(private val repository: MiPastilleroDbRepository) {

    fun invoke(): Flow<List<MedicamentoItem>> {
        return repository.getAllFavoriteMedsFlow()
    }

}
