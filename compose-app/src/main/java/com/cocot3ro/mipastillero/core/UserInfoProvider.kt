package com.cocot3ro.mipastillero.core

import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoProvider @Inject constructor() {
    private var _currentUser: UsuarioItem? = null
    val currentUser: UsuarioItem get() = _currentUser!!.copy()

    fun changeUser(user: UsuarioItem?) {
        _currentUser = user
    }
}