package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.AddMedicamentoUseCase
import com.a23pablooc.proxectofct.domain.usecases.DownloadImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.SearchMedicamentoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddActiveMedViewModel @Inject constructor(
    private val searchMedicamentoUseCase: SearchMedicamentoUseCase,
    private val downloadIMageUseCase: DownloadImageUseCase,
    private val addMedicamentoUseCase: AddMedicamentoUseCase,
    private val userInfoProvider: UserInfoProvider
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

    fun onDataEntered(med: MedicamentoActivoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addMedicamentoUseCase.invoke(med)
        }
    }

    fun getUserId(): Long = userInfoProvider.currentUser.pkUsuario
}