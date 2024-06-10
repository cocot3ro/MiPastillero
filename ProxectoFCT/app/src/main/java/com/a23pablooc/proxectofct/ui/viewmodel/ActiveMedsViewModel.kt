package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.data.network.CimaApiDefinitions
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.AddMedicamentoActivoUseCase
import com.a23pablooc.proxectofct.domain.usecases.DownloadImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.GetMedicamentosActivosUseCase
import com.a23pablooc.proxectofct.domain.usecases.SaveImageUseCase
import com.a23pablooc.proxectofct.domain.usecases.UpdateMedicamentoUseCase
import com.a23pablooc.proxectofct.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveMedsViewModel @Inject constructor(
    private val getMedicamentosActivosUseCase: GetMedicamentosActivosUseCase,
    private val addMedicamentoActivoUseCase: AddMedicamentoActivoUseCase,
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val downloadImageUseCase: DownloadImageUseCase,
    private val userInfoProvider: UserInfoProvider
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState> = _uiState

    // TODO: Hardcode string
    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            getMedicamentosActivosUseCase.invoke()
                .catch {
                    _uiState.value = UiState
                        .Error("Error fetching active meds from DB", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun addActiveMed(med: MedicamentoActivoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val imgPath = med.fkMedicamento.imagen.toString()

            if (imgPath.startsWith(CimaApiDefinitions.BASE_URL)) {
                val imageData = downloadImageUseCase.invoke(
                    med.fkMedicamento.numRegistro,
                    imgPath.substringAfterLast('/')
                )

                val localStoragePath =
                    saveImageUseCase.invoke(
                        "${userInfoProvider.currentUser.pkUsuario}_${med.fkMedicamento.numRegistro}.jpg",
                        imageData
                    )

                med.fkMedicamento.imagen = localStoragePath
            }

            addMedicamentoActivoUseCase.invoke(med)
        }
    }

    fun toggleFavMed(med: MedicamentoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateMedicamentoUseCase.invoke(med.apply { esFavorito = !esFavorito })
        }
    }
}