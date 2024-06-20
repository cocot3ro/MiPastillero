package com.a23pablooc.proxectofct.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.SearchMedicamentoUseCase
import com.a23pablooc.proxectofct.domain.usecases.UpdateMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedInfoViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase,
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase
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
}