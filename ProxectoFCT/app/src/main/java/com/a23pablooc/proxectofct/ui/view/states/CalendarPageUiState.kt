package com.a23pablooc.proxectofct.ui.view.states

import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import java.util.Date

sealed class CalendarPageUiState {
    data object Loading : CalendarPageUiState()
    data class Success(val data: List<MedicamentoCalendarioItem>) : CalendarPageUiState()
    data class Error(val errorMessage: String, val timeStamp: Date, val exception: Throwable) :
        CalendarPageUiState()
}