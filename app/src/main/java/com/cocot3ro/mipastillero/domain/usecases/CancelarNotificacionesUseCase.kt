package com.cocot3ro.mipastillero.domain.usecases

import com.cocot3ro.mipastillero.core.notifications.NotificationManager
import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.NotificacionItem
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class CancelarNotificacionesUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository,
    private val notificationManager: NotificationManager
) {
    suspend fun invoke() {
        val users = pillboxDbRepository.getUsers()
        users.forEach { user ->
            invoke(user)
        }
    }

    suspend fun invoke(user: UsuarioItem) {
        val meds = pillboxDbRepository.getMedicamentosActivos(user)
        meds.forEach { med ->
            invoke(med)
        }
    }

    suspend fun invoke(med: MedicamentoActivoItem) {
        val notificaciones: List<NotificacionItem> =
            pillboxDbRepository.getNotificaciones(med.fkUsuario, med)

        notificaciones.forEach {
            notificationManager.cancelNotification(med, it.timeStamp)
            pillboxDbRepository.deleteNotificacion(it)
        }
    }
}