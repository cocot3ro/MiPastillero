package com.cocot3ro.mipastillero.domain.usecases

import android.content.Context
import com.cocot3ro.mipastillero.core.DataStoreManager
import com.cocot3ro.mipastillero.core.UserInfoProvider
import com.cocot3ro.mipastillero.data.database.DataBaseClearer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DeleteDataUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataBaseClearer: DataBaseClearer,
    private val dataStoreManager: DataStoreManager,
    private val userInfoProvider: UserInfoProvider,
    private val cancelarNotificacionesUseCase: CancelarNotificacionesUseCase
) {

    suspend fun invoke() {
        cancelarNotificacionesUseCase.invoke()
        dataBaseClearer.clearAllData()
        dataStoreManager.clear()

        context.filesDir.list()?.forEach {
            File(context.filesDir, it).deleteRecursively()
        }

        userInfoProvider.changeUser(null)
    }
}