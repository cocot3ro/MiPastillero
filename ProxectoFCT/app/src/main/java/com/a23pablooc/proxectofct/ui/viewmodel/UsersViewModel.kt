package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.usecases.CreateUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.GetUsersUseCase
import com.a23pablooc.proxectofct.domain.usecases.SelectDefaultUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.SelectUserUseCase
import com.a23pablooc.proxectofct.ui.view.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private val dataStoreManager: DataStoreManager,
    private val selectDefaultUserUseCase: SelectDefaultUserUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _onChangeDefaultUserFlow: MutableStateFlow<Long> = MutableStateFlow(DataStoreManager.Defaults.DEFAULT_USER_ID)
    val onChangeDefaultUserFlow: StateFlow<Long> = _onChangeDefaultUserFlow

    fun selectUser(user: UsuarioItem) {
        viewModelScope.launch(Dispatchers.Main) {
            selectUserUseCase.invoke(user)
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

    suspend fun createUser(user: UsuarioItem): Long {
        return createUserUseCase.invoke(user)
    }

    suspend fun getDefaultUserId(): Long {
        return dataStoreManager.getUserToLoginId() ?: dataStoreManager.defaultUserId().first()
    }

    suspend fun changeDefaultUser(userId: Long) {
        selectDefaultUserUseCase.invoke(userId)
        _onChangeDefaultUserFlow.emit(userId)
    }

    fun trigger() {
        viewModelScope.launch(Dispatchers.IO) {
            _onChangeDefaultUserFlow.emit(getDefaultUserId())
        }
    }
}