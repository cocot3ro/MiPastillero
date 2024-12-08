package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository
) {

    fun invoke(): Flow<List<UsuarioItem>> {
        return miPastilleroDbRepository.getUsersFlow()
    }

}