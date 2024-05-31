package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.GetMedicamentosActivosUseCase
import com.a23pablooc.proxectofct.ui.view.states.MainScreenUiState
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
    private val getMedicamentosActivosUseCase: GetMedicamentosActivosUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState.Loading)

    val uiState: StateFlow<MainScreenUiState> = _uiState

    // TODO: Hardcode string on error message
    fun fetchData() {
        viewModelScope.launch {
            getMedicamentosActivosUseCase()
                .catch {
                    _uiState.value = MainScreenUiState
                        .Error("Error fetching active meds from DB", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = MainScreenUiState.Success(it)
                }
        }
    }
}