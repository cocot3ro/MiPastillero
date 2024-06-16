package com.a23pablooc.proxectofct.core

import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoProvider @Inject constructor() {
    private lateinit var _currentUser: UsuarioItem
    val currentUser: UsuarioItem get() = _currentUser.copy()

    fun changeUser(user: UsuarioItem) {
        _currentUser = user
    }
}