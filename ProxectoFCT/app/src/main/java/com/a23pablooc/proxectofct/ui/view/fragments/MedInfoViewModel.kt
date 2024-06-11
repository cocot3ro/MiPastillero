package com.a23pablooc.proxectofct.ui.view.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.UpdateMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedInfoViewModel @Inject constructor(
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase
) : ViewModel() {

    fun toggleFavMed(med: MedicamentoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateMedicamentoUseCase.invoke(med.apply { esFavorito = !esFavorito })
        }
    }
}