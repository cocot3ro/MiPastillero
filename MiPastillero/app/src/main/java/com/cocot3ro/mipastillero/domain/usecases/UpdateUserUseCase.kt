package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {
    suspend fun invoke(user: UsuarioItem) {
        pillboxDbRepository.updateUser(user)
    }
}
