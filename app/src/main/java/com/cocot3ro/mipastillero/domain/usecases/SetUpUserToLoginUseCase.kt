package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.DataStoreManager
import javax.inject.Inject

class SetUpUserToLoginUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend fun invoke(userId: Long) {
        dataStoreManager.setUpUserToLoginId(userId)
    }
}