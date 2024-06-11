package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DataStoreManager
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    suspend fun invoke(mapSettings: MutableMap<String, *>) {
        dataStoreManager.saveSettings(mapSettings)
    }
}
