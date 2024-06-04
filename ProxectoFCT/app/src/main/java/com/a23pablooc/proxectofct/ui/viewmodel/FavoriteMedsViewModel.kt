package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.usecases.GetFavoriteMedsUseCase
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
class FavoriteMedsViewModel @Inject constructor(
    private val getFavoriteMedsUseCase: GetFavoriteMedsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState> = _uiState

    fun fetchData() {
        viewModelScope.launch {
            getFavoriteMedsUseCase.invoke()
                .catch {
                    _uiState.value = UiState
                        .Error("Error fetching favorite meds", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}