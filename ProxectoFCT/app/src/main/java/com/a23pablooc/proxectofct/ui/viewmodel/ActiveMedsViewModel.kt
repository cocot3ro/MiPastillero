package com.a23pablooc.proxectofct.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.domain.SaveErrorUseCase
import com.a23pablooc.proxectofct.ui.view.states.MainScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ActiveMedsViewModel @Inject constructor(
    private val saveErrorUseCase: SaveErrorUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainScreenUiState> =
        MutableStateFlow(MainScreenUiState.Loading)

    val uiState: StateFlow<MainScreenUiState> = _uiState

    fun fetchData() {

    }

    fun saveError(context: Context, errorMessage: String, timeStamp: Date, exception: Throwable) {
        saveErrorUseCase(context, errorMessage, timeStamp, exception)
    }
}