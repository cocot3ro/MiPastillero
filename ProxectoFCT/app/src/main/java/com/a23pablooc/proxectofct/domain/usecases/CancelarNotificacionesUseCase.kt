package com.a23pablooc.proxectofct.domain.usecases

import com.a23pablooc.proxectofct.core.notifications.NotificationManager
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.NotificacionItem
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
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
            notificationManager.cancelNotification(med, it.dia, it.hora)
            pillboxDbRepository.deleteNotificacion(it)
        }
    }
}