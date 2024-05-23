package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.GetMedicamentosCalendarioUseCase
import com.a23pablooc.proxectofct.ui.view.states.CalendarPageUiState
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
    private val getMedicamentosCalendarioUseCase: GetMedicamentosCalendarioUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<CalendarPageUiState> =
        MutableStateFlow(CalendarPageUiState.Loading)

    val uiState: StateFlow<CalendarPageUiState> = _uiState

    //    TODO: Hardcode string on error message
    fun fetchData(date: Date) {
        viewModelScope.launch {
            getMedicamentosCalendarioUseCase(date)
                .catch { _uiState.value = CalendarPageUiState.Error("Error fetching data", it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = CalendarPageUiState.Success(it)
                }
        }
    }
}