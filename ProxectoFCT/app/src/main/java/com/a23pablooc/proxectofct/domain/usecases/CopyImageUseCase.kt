package com.a23pablooc.proxectofct.domain.usecases

import java.io.File
import javax.inject.Inject

class CopyImageUseCase @Inject constructor() {

    operator fun invoke(fileToCopy: File, pathToSave: File, fileNameToSave: String): String {
        if (!fileToCopy.exists()) {
            throw IllegalArgumentException("File to copy does not exist")
        }

        if (!pathToSave.exists()) {
            pathToSave.mkdirs()
        }

        val file = File(pathToSave, fileNameToSave)
        file.outputStream().use { it.write(fileToCopy.readBytes()) }

        return file.absolutePath
    }
}