package com.cocot3ro.mipastillero.core.user_info

class UserInfoProvider {

    var loggedUser: LoggedUser? = null
        private set
        get() {
            return field?.copy()
        }

    fun setUser(user: LoggedUser?) {
        loggedUser = user
    }
}
