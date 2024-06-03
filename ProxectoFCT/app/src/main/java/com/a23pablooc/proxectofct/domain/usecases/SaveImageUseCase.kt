package com.a23pablooc.proxectofct.domain.usecases

import java.io.File
import javax.inject.Inject

class SaveImageUseCase @Inject constructor() {
    operator fun invoke(folder: File, fileName: String, imageData: ByteArray): String {
        if (!folder.exists()) {
            folder.mkdir()
        }

        val file = File(folder, fileName)
        file.outputStream().use { it.write(imageData) }

        return file.absolutePath
    }
}