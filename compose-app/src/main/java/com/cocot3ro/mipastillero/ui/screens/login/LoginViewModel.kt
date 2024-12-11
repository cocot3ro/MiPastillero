package com.cocot3ro.mipastillero.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import com.cocot3ro.mipastillero.domain.usecases.CreateUserUseCase
import com.cocot3ro.mipastillero.domain.usecases.GetDefaultUserUseCase
import com.cocot3ro.mipastillero.domain.usecases.GetUsersUseCase
import com.cocot3ro.mipastillero.domain.usecases.SelectDefaultUserUseCase
import com.cocot3ro.mipastillero.domain.usecases.SelectUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getDefaultUserUseCase: GetDefaultUserUseCase,
    private val selectUserUseCase: SelectUserUseCase,
    private val selectDefaultUserUseCase: SelectDefaultUserUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    val usersFlow: Flow<List<UsuarioItem>> by lazy { getUsersUseCase.invoke() }
    val defaultUserFlow: Flow<Long?> by lazy { getDefaultUserUseCase.invoke() }

    fun createUser(newUser: UsuarioItem, default: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            createUserUseCase.invoke(newUser)
            if (default) {
                selectDefaultUserUseCase.invoke(newUser.pkUsuario)
            }
        }
    }

    fun selectUser(user: UsuarioItem) {
        selectUserUseCase.invoke(user)
    }

}