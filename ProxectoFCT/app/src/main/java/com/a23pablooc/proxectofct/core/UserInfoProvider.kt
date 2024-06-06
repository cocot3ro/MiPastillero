package com.a23pablooc.proxectofct.core

import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoProvider @Inject constructor() {
    private lateinit var _currentUser: UsuarioItem
    val currentUser: UsuarioItem get() = _currentUser

    private val _userChangedFlow = MutableSharedFlow<UsuarioItem>()
    val userChangedFlow: SharedFlow<UsuarioItem> = _userChangedFlow

    suspend fun changeUser(user: UsuarioItem) {
        _currentUser = user
        _userChangedFlow.emit(user)
    }
}