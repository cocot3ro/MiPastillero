package com.a23pablooc.proxectofct.ui.view.states

import com.a23pablooc.proxectofct.domain.model.MedicamentoFavoritoItem
import java.util.Date

sealed class FavoriteFragmentUiState {
    data object Loading : FavoriteFragmentUiState()
    data class Success(val data: List<MedicamentoFavoritoItem>) : FavoriteFragmentUiState()
    data class Error(val errorMessage: String, val timeStamp: Date, val exception: Throwable) : FavoriteFragmentUiState()
}