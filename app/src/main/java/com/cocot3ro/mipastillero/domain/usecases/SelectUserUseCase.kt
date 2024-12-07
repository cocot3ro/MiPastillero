package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.UserInfoProvider
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class SelectUserUseCase @Inject constructor(
    private val userInfoProvider: UserInfoProvider
) {
    fun invoke(newUser: UsuarioItem) {
        userInfoProvider.changeUser(newUser)
    }
}
