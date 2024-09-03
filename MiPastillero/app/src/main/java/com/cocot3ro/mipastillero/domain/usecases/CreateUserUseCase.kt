package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val pillboxDbRepository: PillboxDbRepository) {
    suspend fun invoke(user: UsuarioItem): Long {
        return pillboxDbRepository.createUser(user)
    }

}
