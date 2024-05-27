package com.a23pablooc.proxectofct.ui.view.viewholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.SearchMedicamentoUseCase
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddActiveMedDialogViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase
) : ViewModel() {

    private val codNacionalPattern = Regex("""^[(6-9)]\d{5}(\.\d)?$""")

    fun search(codNacional: String): MedicamentoItem? {
        if (!codNacionalPattern.matches(codNacional)) throw IllegalArgumentException("Invalid codNacional")

        var medicamentoItem: MedicamentoItem? = null

        viewModelScope.launch(Dispatchers.IO) {
            medicamentoItem = searchMedicamentoUseCase.invoke(codNacional)
        }

        return medicamentoItem
    }

}