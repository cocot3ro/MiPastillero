package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {
    suspend fun invoke(user: UsuarioItem) {
        pillboxDbRepository.updateUser(user)
    }
}