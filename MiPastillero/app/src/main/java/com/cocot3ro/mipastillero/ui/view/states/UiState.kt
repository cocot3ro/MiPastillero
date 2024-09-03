package com.cocot3ro.mipastillero.ui.view.states

sealed class UiState {
    data object Loading : UiState()
    data class Success<T>(val data: List<T>) : UiState()
    data class Error(val errorMessage: String, val exception: Throwable) : UiState()
}