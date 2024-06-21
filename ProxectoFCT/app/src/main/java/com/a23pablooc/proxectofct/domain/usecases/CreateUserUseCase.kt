package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val pillboxDbRepository: PillboxDbRepository) {
    suspend fun invoke(user: UsuarioItem): Long {
        return pillboxDbRepository.createUser(user)
    }

}
