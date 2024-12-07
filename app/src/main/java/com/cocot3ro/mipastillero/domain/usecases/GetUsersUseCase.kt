package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {

    fun invoke(): Flow<List<UsuarioItem>> {
        return pillboxDbRepository.getUsersFlow()
    }

}