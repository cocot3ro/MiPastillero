package com.cocot3ro.mipastillero.core.user_info

class UserRepository(
    private val userInfoProvider: UserInfoProvider
) {

    fun logIn(userId: Long) {
        userInfoProvider.setUser(LoggedUser(userId))
    }

    fun logOut() {
        userInfoProvider.setUser(null)
    }

    fun getUser(): Long? {
        return userInfoProvider.loggedUser?.userId
    }

}