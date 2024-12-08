package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.DataStoreManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SelectDefaultUserUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend fun invoke(userId: Long) {
        dataStoreManager.defaultUserId(
            if (userId == dataStoreManager.defaultUserId()
                    .first()
            ) DataStoreManager.Defaults.DEFAULT_USER_ID else userId
        )
    }

}
