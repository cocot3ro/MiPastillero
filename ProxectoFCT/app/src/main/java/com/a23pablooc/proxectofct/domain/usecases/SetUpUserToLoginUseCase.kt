package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DataStoreManager
import javax.inject.Inject

class SetUpUserToLoginUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend fun invoke(userId: Long) {
        dataStoreManager.setUpUserToLoginId(userId)
    }
}