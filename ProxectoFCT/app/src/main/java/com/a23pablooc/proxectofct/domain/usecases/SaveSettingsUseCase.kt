package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.DataStoreManager
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val programarNotificacionesUseCase: ProgramarNotificacionesUseCase,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase
) {
    suspend fun invoke(mapSettings: MutableMap<String, *>) {
        dataStoreManager.saveSettings(mapSettings)

        if (mapSettings[DataStoreManager.PreferencesKeys.USE_NOTIFICATIONS] as Boolean)
            programarNotificacionesUseCase.invoke()
        else
            cancelarNotificacionesUseCase.invoke()
    }
}