package com.cocot3ro.mipastillero.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.cocot3ro.mipastillero.core.DataStoreManager
import com.cocot3ro.mipastillero.data.network.CimaApiDefinitions
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.domain.usecases.DownloadImageUseCase
import com.cocot3ro.mipastillero.domain.usecases.SaveImageUseCase
import com.cocot3ro.mipastillero.domain.usecases.SearchMedicamentoUseCase
import com.cocot3ro.mipastillero.domain.usecases.UpdateMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MedInfoViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase,
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase,
    private val downloadImageUseCase: DownloadImageUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun openUrl(context: Context, url: Uri) {
        val browserIntent = Intent(Intent.ACTION_VIEW, url)
        context.startActivity(browserIntent)
    }

    suspend fun search(codNacional: String): MedicamentoItem? {
        if (!CimaApiDefinitions.codNacionalPattern.matches(codNacional))
            throw IllegalArgumentException("Invalid codNacional")

        return searchMedicamentoUseCase.invoke(codNacional.substringBefore('.').toLong())
    }

    suspend fun updateCodNacional(oldCodNacional: Long, med: MedicamentoItem) {
        updateMedicamentoUseCase.invoke(oldCodNacional, med)
    }

    suspend fun useOfficialImage(med: MedicamentoItem): Uri {
        val imgPath = med.imagen.toString()

        return if (imgPath.startsWith(CimaApiDefinitions.BASE_URL)) {
            if (dataStoreManager.useImages().first()) {

                val imageData = downloadImageUseCase.invoke(
                    med.numRegistro,
                    imgPath.substringAfterLast('/')
                )

                val localStoragePath =
                    saveImageUseCase.invoke(
                        "${med.numRegistro}.${
                            imgPath.substringAfterLast(
                                '.'
                            )
                        }", imageData
                    )

                localStoragePath
            } else {
                Uri.EMPTY
            }
        } else {
            Uri.parse(imgPath)
        }
    }
}