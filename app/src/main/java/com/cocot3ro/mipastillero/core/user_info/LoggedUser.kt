package com.cocot3ro.mipastillero.core.user_info

@JvmInline
value class LoggedUser(
    val userId: Long
) {
    fun copy(userId: Long = this.userId): LoggedUser {
        return LoggedUser(userId)
    }
}