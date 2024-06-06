package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.usecases.GetMedicamentosCalendarioUseCase
import com.a23pablooc.proxectofct.domain.usecases.MarcarTomaUseCase
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarPageViewModel @Inject constructor(
    private val getMedicamentosCalendarioUseCase: GetMedicamentosCalendarioUseCase,
    private val marcarTomaUseCase: MarcarTomaUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState> = _uiState

    // TODO: Hardcode string
    fun fetchData(date: Date) {
        viewModelScope.launch(Dispatchers.Main) {
            getMedicamentosCalendarioUseCase.invoke(date)
                .catch {
                    _uiState.value =
                        UiState.Error("Error fetching calendar meds from DB", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun marcarToma(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        viewModelScope.launch {
            marcarTomaUseCase.invoke(med, dia, hora)
        }
    }
}