package com.a23pablooc.proxectofct.domain.usecases

import android.content.Context
import com.a23pablooc.proxectofct.core.InternalStorageDefinitions
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pillboxDbRepository: PillboxDbRepository,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase
) {
    suspend fun invoke(user: UsuarioItem) {
        cancelarNotificacionesUseCase.invoke(user)

        pillboxDbRepository.deleteUser(user)

        val imagesDirectory = File(context.filesDir, InternalStorageDefinitions.IMAGES_DIRECTORY)

        val userDirectory = File(imagesDirectory, user.pkUsuario.toString())

        userDirectory.deleteRecursively()
    }
}