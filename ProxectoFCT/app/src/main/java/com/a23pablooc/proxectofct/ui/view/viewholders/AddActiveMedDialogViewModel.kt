package com.a23pablooc.proxectofct.ui.view.viewholders

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.DownloadImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.SearchMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddActiveMedDialogViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase,
    private val downloadIMageUseCase: DownloadImageUseCase
) : ViewModel() {

    private val codNacionalPattern = Regex("""[6-9]\d{5}(\.\d)?""")

    suspend fun search(codNacional: String): MedicamentoItem? {
        if (!codNacionalPattern.matches(codNacional))
            throw IllegalArgumentException("Invalid codNacional") // TODO: Hardcoded string

        return searchMedicamentoUseCase.invoke(codNacional.substringBefore('.').toLong())
    }

    suspend fun downloadImage(nregistro: String, imgResource: String): ByteArray {
        return downloadIMageUseCase.invoke(nregistro, imgResource)
    }
}