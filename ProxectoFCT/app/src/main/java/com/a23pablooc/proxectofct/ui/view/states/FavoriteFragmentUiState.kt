package com.a23pablooc.proxectofct.ui.view.states

import com.a23pablooc.proxectofct.domain.model.MedicamentoFavoritoItem

sealed class FavoriteFragmentUiState {
    data object Loading : FavoriteFragmentUiState()
    data class Success(val data: List<MedicamentoFavoritoItem>) : FavoriteFragmentUiState()
    data class Error(val errorMessage: String, val error: Throwable) : FavoriteFragmentUiState()
}