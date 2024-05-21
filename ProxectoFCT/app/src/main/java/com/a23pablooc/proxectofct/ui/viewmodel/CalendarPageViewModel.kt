package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.domain.GetMedicamentosCalendarioUseCase
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import com.a23pablooc.proxectofct.ui.view.states.CalendarPageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalendarPageViewModel @Inject constructor(
    getMedicamentosCalendarioUseCase: GetMedicamentosCalendarioUseCase
) : ViewModel() {
    private val _meds: Flow<List<MedicamentoCalendarioItem>> = getMedicamentosCalendarioUseCase()

    private val _uiState: MutableStateFlow<CalendarPageUiState> = MutableStateFlow(CalendarPageUiState.Loading)
    val uiState: StateFlow<CalendarPageUiState> = _uiState
}