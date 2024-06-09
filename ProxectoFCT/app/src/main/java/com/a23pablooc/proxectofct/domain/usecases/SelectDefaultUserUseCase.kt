package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DataStoreManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SelectDefaultUserUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(userId: Long) {
        dataStoreManager.defaultUserId(
            if (userId == dataStoreManager.defaultUserId()
                    .first()
            ) DataStoreManager.Defaults.DEFAULT_USER_ID else userId
        )
    }

}
