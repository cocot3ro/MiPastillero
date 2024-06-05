package com.a23pablooc.proxectofct.ui.view.viewholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.usecases.GetUsersUseCase
import com.a23pablooc.proxectofct.domain.usecases.SelectUserUseCase
import com.a23pablooc.proxectofct.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val selectUserUseCase: SelectUserUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _manageFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val manageFlow: MutableSharedFlow<Boolean> = _manageFlow

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun selectUser(user: UsuarioItem) {
        selectUserUseCase(user)
    }

    // TODO: Hardcode string
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            getUsersUseCase.invoke()
                .catch {
                    _uiState.value =
                        UiState.Error("Error fetching users from DB", it)
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }
}