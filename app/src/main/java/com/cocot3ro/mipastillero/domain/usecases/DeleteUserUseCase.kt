package com.cocot3ro.mipastillero.domain.usecases

import android.content.Context
import com.cocot3ro.mipastillero.core.InternalStorageDefinitions
import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
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