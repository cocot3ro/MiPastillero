package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.UserInfoProvider
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import javax.inject.Inject

class SelectUserUseCase @Inject constructor() {
    operator fun invoke(newUser: UsuarioItem) {
        UserInfoProvider.currentUser = newUser
    }
}
