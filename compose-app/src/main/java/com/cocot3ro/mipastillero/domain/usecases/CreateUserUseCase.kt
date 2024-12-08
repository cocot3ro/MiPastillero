package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository
) {
    suspend fun invoke(user: UsuarioItem): Long {
        return miPastilleroDbRepository.createUser(user)
    }

}
