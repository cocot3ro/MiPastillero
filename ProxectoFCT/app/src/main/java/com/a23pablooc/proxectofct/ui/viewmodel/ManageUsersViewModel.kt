package com.a23pablooc.proxectofct.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import com.a23pablooc.proxectofct.domain.usecases.CreateUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.DeleteUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.GetUsersUseCase
import com.a23pablooc.proxectofct.domain.usecases.SelectDefaultUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.SelectUserUseCase
import com.a23pablooc.proxectofct.domain.usecases.UpdateUserUseCase
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
class ManageUsersViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val selectDefaultUserUseCase: SelectDefaultUserUseCase,
    private val dataStoreManager: DataStoreManager,
    private val userInfoProvider: UserInfoProvider,
    private val selectUserUseCase: SelectUserUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _onChangeDefaultUserFlow: MutableStateFlow<Long> =
        MutableStateFlow(DataStoreManager.Defaults.DEFAULT_USER_ID)
    val onChangeDefaultUserFlow: StateFlow<Long> = _onChangeDefaultUserFlow

    private val _onSaveChangesFlow: MutableStateFlow<Any> = MutableStateFlow(Unit)
    val onSaveChangesFlow: StateFlow<Any> = _onSaveChangesFlow

    private val _changes = mutableSetOf<Long>()
    val hasChanges get(): Boolean = _changes.isNotEmpty()

    fun changeDefaultUser(userId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            selectDefaultUserUseCase.invoke(userId)
            _onChangeDefaultUserFlow.emit(userId)
        }
    }

    fun fetchData(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            getUsersUseCase.invoke()
                .catch {
                    _uiState.value =
                        UiState.Error(context.getString(R.string.error_fetching_users), it)
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

    private suspend fun getDefaultUserId(): Long {
        return dataStoreManager.defaultUserId().first()
    }

    fun trigger() {
        viewModelScope.launch(Dispatchers.Main) {
            _onChangeDefaultUserFlow.emit(getDefaultUserId())
        }
    }

    fun saveUser(user: UsuarioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserUseCase.invoke(user)

            if (user.pkUsuario == userInfoProvider.currentUser.pkUsuario) {
                selectUserUseCase.invoke(user)
            }
        }
    }

    fun triggerSave() {
        viewModelScope.launch(Dispatchers.Main) {
            _onSaveChangesFlow.emit(Any())
        }
    }

    fun deleteUser(usuario: UsuarioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserUseCase.invoke(usuario)
        }
    }

    fun registerChange(userId: Long, hasChanged: Boolean) {
        if (hasChanged) _changes.add(userId)
        else _changes.remove(userId)
    }
}