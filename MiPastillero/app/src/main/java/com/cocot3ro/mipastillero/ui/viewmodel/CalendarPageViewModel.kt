package com.cocot3ro.mipastillero.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.usecases.GetMedicamentosCalendarioUseCase
import com.cocot3ro.mipastillero.domain.usecases.MarcarTomaUseCase
import com.cocot3ro.mipastillero.ui.view.states.UiState
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

    fun fetchData(context: Context, date: Date) {
        viewModelScope.launch(Dispatchers.Main) {
            getMedicamentosCalendarioUseCase.invoke(date)
                .catch {
                    _uiState.value =
                        UiState.Error(context.getString(R.string.error_fetching_meds), it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun marcarToma(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            marcarTomaUseCase.invoke(med, dia, hora)
        }
    }
}