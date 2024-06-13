package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.AgendaItem
import com.a23pablooc.proxectofct.domain.usecases.GetDiaryUseCase
import com.a23pablooc.proxectofct.domain.usecases.SaveDiaryUseCase
import com.a23pablooc.proxectofct.ui.view.states.UiState
import com.google.gson.Gson
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
class DiaryPageViewModel @Inject constructor(
    val gson: Gson,
    val userInfoProvider: UserInfoProvider,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val saveDiaryUseCase: SaveDiaryUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchData(date: Date) {
        viewModelScope.launch(Dispatchers.Main) {
            getDiaryUseCase.invoke(date)
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

    fun saveDiaryEntry(also: AgendaItem) {
        viewModelScope.launch(Dispatchers.IO) {
            saveDiaryUseCase.invoke(also)
        }
    }
}