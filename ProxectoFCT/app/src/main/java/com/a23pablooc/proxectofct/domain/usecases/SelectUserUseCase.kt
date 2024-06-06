package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import javax.inject.Inject

class SelectUserUseCase @Inject constructor(
    private val userInfoProvider: UserInfoProvider
) {
    suspend operator fun invoke(newUser: UsuarioItem) {
        userInfoProvider.changeUser(newUser)
    }
}
