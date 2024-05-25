package com.a23pablooc.proxectofct.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.GetFavoriteMedsUseCase
import com.a23pablooc.proxectofct.domain.SaveErrorUseCase
import com.a23pablooc.proxectofct.ui.view.states.MainScreenUiState
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
class FavoriteMedsViewModel @Inject constructor(
    private val getFavoriteMedsUseCase: GetFavoriteMedsUseCase,
    private val saveErrorUseCase: SaveErrorUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState.Loading)

    val uiState: StateFlow<MainScreenUiState> = _uiState

    fun fetchData() {
        viewModelScope.launch {
            getFavoriteMedsUseCase()
                .catch {
                    _uiState.value = MainScreenUiState.Error(
                        "Error fetching favorite meds",
                        Date(),
                        it
                    )
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = MainScreenUiState.Success(it)
                }
        }
    }

    fun saveError(
        requireContext: Context,
        errorMessage: String,
        timeStamp: Date,
        exception: Throwable
    ) {
        saveErrorUseCase(requireContext, errorMessage, timeStamp, exception)
    }
}