package com.a23pablooc.proxectofct.ui.view.viewholders

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.core.InternalStorageDefinitions
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.DownloadImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.SaveImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.SearchMedicamentoUseCase
import com.a23pablooc.proxectofct.ui.view.fragments.AddActiveMedDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddActiveMedDialogViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase,
    private val downloadImageUseCase: DownloadImageUseCase,
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val codNacionalPattern = Regex("""[6-9]\d{5}(\.\d)?""")

    suspend fun search(codNacional: String): MedicamentoItem? {
        if (!codNacionalPattern.matches(codNacional))
            throw IllegalArgumentException("Invalid codNacional") // TODO: Hardcoded string

        return searchMedicamentoUseCase.invoke(codNacional.substringBefore('.').toLong())
    }

    suspend fun downloadAndSaveImage(
        context: Context,
        numRegistro: String,
        imgResource: String
    ): String? {
        val folder = File(context.filesDir, InternalStorageDefinitions.TEMP_FOLDER)
        val fileName = "temp_${AddActiveMedDialogFragment.TAG}_$numRegistro.jpg"

        val file = File(folder, fileName)
        if (file.exists()) return file.absolutePath

        val data = downloadImageUseCase.invoke(numRegistro, imgResource)

        if (data.isEmpty()) return null

        return saveImageUseCase.invoke(folder, fileName, data)
    }

    private fun saveImage(context: Context, imageData: ByteArray): String {
        val folder = File(context.filesDir, InternalStorageDefinitions.TEMP_FOLDER)
        val fileName = "temp_${AddActiveMedDialogFragment.TAG}.jpg"

        return saveImageUseCase.invoke(folder, fileName, imageData)
    }

    fun saveImage(context: Context, imageUri: Uri): String {
        val imageData = uriToByteArray(context, imageUri)
            ?: throw IllegalArgumentException("Invalid imageData")
        return saveImage(context, imageData)
    }

    private fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.readBytes()
        } catch (e: Exception) {
            Log.e("AddActiveMedDialogVM", "Error converting URI to ByteArray", e)
            null
        }
    }
}