package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.usecases.CreateUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.GetUsersUseCase
import com.a23pablooc.proxectofct.domain.usecases.SelectUserUseCase
import com.a23pablooc.proxectofct.ui.view.fragments.UsersFragment
import com.a23pablooc.proxectofct.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val selectUserUseCase: SelectUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _manageFlow: MutableSharedFlow<UsersFragment.FragmentMode> = MutableSharedFlow()
    val manageFlow: MutableSharedFlow<UsersFragment.FragmentMode> = _manageFlow

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    fun selectUser(user: UsuarioItem) {
        viewModelScope.launch(Dispatchers.Main) {
            selectUserUseCase.invoke(user)
        }
    }

    fun manageUsers(value: UsersFragment.FragmentMode) {
        viewModelScope.launch(Dispatchers.Main) {
            _manageFlow.emit(value)
        }
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            getUsersUseCase.invoke()
                .catch {
                    _uiState.value =
                        UiState.Error("Error fetching users from DB", it) // TODO: Hardcode string
                }
                .flowOn(Dispatchers.IO)
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun createUser(user: UsuarioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.invoke(user)
        }
    }

    suspend fun hasDefaultUser(): Boolean {
        return dataStoreManager.hasDefaultUserId().first()
    }

    suspend fun defaultUserId(): Long {
        return dataStoreManager.defaultUserId().first()
    }
}