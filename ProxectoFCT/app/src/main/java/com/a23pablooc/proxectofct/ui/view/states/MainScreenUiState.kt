package com.a23pablooc.proxectofct.ui.view.states

sealed class MainScreenUiState {
    data object Loading : MainScreenUiState()
    data class Success<T>(val data: List<T>) : MainScreenUiState()
    data class Error(val errorMessage: String, val exception: Throwable) : MainScreenUiState()
}