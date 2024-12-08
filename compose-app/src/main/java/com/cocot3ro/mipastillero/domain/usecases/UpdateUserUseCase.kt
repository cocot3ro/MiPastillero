package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.data.repositories.MiPastilleroDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val miPastilleroDbRepository: MiPastilleroDbRepository
) {
    suspend fun invoke(user: UsuarioItem) {
        miPastilleroDbRepository.updateUser(user)
    }
}
