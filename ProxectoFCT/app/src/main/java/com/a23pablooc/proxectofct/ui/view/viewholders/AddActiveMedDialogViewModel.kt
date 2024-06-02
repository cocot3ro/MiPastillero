package com.a23pablooc.proxectofct.ui.view.viewholders

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.domain.SearchMedicamentoUseCase
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddActiveMedDialogViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase
) : ViewModel() {

    private val codNacionalPattern = Regex("""[6-9]\d{5}(\.\d)?""")

    suspend fun search(codNacional: String): MedicamentoItem? {
        if (!codNacionalPattern.matches(codNacional))
            throw IllegalArgumentException("Invalid codNacional")

        return searchMedicamentoUseCase.invoke(codNacional.substringBefore('.').toInt())
    }
}