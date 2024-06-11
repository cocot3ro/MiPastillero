package com.a23pablooc.proxectofct.domain.usecases

import android.content.Context
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pillboxDbRepository: PillboxDbRepository
) {
    suspend fun invoke(user: UsuarioItem) {
        pillboxDbRepository.deleteUser(user)

        val file = File(context.filesDir, user.pkUsuario.toString())

        file.deleteRecursively()
    }
}