package com.a23pablooc.proxectofct.domain.usecases

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.a23pablooc.proxectofct.core.InternalStorageDefinitions
import com.a23pablooc.proxectofct.core.UserInfoProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userInfoProvider: UserInfoProvider
) {
    fun invoke(fileName: String, imageData: ByteArray): Uri {
        val imagesDirectory = File(context.filesDir, InternalStorageDefinitions.IMAGES_DIRECTORY)

        val userDirectory = File(imagesDirectory, userInfoProvider.currentUser.pkUsuario.toString())

        userDirectory.mkdirs()

        val imageFile = File(userDirectory, fileName)

        imageFile.outputStream().use { it.write(imageData) }

        return imageFile.toUri()
    }
}