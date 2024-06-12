package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.MedicamentoItem
import com.a23pablooc.proxectofct.domain.usecases.GetMedicamentosActivosUseCase
import com.a23pablooc.proxectofct.domain.usecases.UpdateMedicamentoUseCase
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.google.gson.Gson
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
    val gson: Gson,
    val userInfoProvider: UserInfoProvider,
    private val getMedicamentosActivosUseCase: GetMedicamentosActivosUseCase,
    private val updateMedicamentoUseCase: UpdateMedicamentoUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
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

    fun toggleFavMed(med: MedicamentoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateMedicamentoUseCase.invoke(med.apply { esFavorito = !esFavorito })
        }
    }
}