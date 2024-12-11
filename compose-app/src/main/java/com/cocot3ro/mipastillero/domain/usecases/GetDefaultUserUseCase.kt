package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.datastore.DataStoreManager
import javax.inject.Inject

class GetDefaultUserUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    fun invoke() = dataStoreManager.defaultUserId()

}