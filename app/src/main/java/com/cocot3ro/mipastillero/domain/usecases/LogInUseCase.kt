package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.user_info.UserRepository

class LogInUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(userId: Long): Boolean {
        // TODO: Check if the user exists in the database.
        //  If doesn't exist, check if the default user value
        //  stored in the preferences is not this user. In that case remove it.
        userRepository.logIn(userId)
        return true
    }
}