package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.GetFavoriteMedsUseCase
import com.a23pablooc.proxectofct.ui.view.states.FavoriteFragmentUiState
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

    private val _uiState: MutableStateFlow<FavoriteFragmentUiState> =
        MutableStateFlow(FavoriteFragmentUiState.Loading)

    val uiState: StateFlow<FavoriteFragmentUiState> = _uiState

    fun fetchData() {
        viewModelScope.launch {
            getFavoriteMedsUseCase()
                .catch { _uiState.value = FavoriteFragmentUiState.Error("Error fetching data", it) }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = FavoriteFragmentUiState.Success(it)
                }
        }
    }
}