package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository
) {

    fun invoke(): Flow<List<UsuarioItem>> {
        return pillboxDbRepository.getUsers()
    }

}