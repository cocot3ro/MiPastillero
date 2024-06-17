package com.a23pablooc.proxectofct.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.DateTimeUtils.get
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.usecases.GetMedicamentosCalendarioUseCase
import com.a23pablooc.proxectofct.domain.usecases.MarcarTomaUseCase
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
        viewModelScope.launch(Dispatchers.IO) {
            val timeStamp = Calendar.getInstance().apply {
                set(Calendar.YEAR, dia.get(Calendar.YEAR))
                set(Calendar.MONTH, dia.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, dia.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, hora.get(Calendar.MINUTE))
                set(Calendar.SECOND, hora.get(Calendar.SECOND))
                set(Calendar.MILLISECOND, hora.get(Calendar.MILLISECOND))
            }.time

            marcarTomaUseCase.invoke(med, timeStamp)
        }
    }
}