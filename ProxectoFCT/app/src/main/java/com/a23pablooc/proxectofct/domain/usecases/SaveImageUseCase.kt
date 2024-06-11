package com.a23pablooc.proxectofct.domain.usecases

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun invoke(fileName: String, imageData: ByteArray): Uri {
        val file = File(context.filesDir, fileName)

        file.outputStream().use { it.write(imageData) }

        return file.toUri()
    }
}