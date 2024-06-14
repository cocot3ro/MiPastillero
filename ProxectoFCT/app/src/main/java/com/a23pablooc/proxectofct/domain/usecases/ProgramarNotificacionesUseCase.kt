package com.a23pablooc.proxectofct.domain.usecases

import android.icu.util.Calendar
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.core.notifications.NotificationDefinitions
import com.a23pablooc.proxectofct.core.notifications.NotificationManager
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import javax.inject.Inject

class ProgramarNotificacionesUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository,
    private val notificationManager: NotificationManager
) {
    suspend fun invoke(med: MedicamentoActivoItem) {
        val notifications = pillboxDbRepository.getNotificaciones(med)

        val alreadyNotifiedNotifications = notifications.filter { it.notificado }
        val notNotifiedNotifications = notifications.filter { !it.notificado }

        if (notNotifiedNotifications.size >= NotificationDefinitions.NOTIFICATIONS_PER_MED) return

        val horario = med.horario.sorted().toList()

        val lastNotification = alreadyNotifiedNotifications.lastOrNull()

        val calendar = Calendar.getInstance()

        if (lastNotification == null) {
            calendar.time = med.fechaInicio.zeroTime()

            var createdNotifications = 0

            while (createdNotifications < NotificationDefinitions.NOTIFICATIONS_PER_MED) {
                for (hora in horario) {
                    if (createdNotifications++ >= NotificationDefinitions.NOTIFICATIONS_PER_MED) break

                    notificationManager.scheduleNotification(med, calendar.time.zeroTime(), hora)

                    calendar.add(Calendar.DAY_OF_MONTH, 1)

                    if (calendar.time.zeroTime() >= med.fechaFin.zeroTime()) break
                }
            }
        } else {
            calendar.time = lastNotification.fecha.zeroTime()

            var createdNotifications = 0

            var idx = lastNotification.hora.let { horario.indexOf(it) }

            while (createdNotifications < NotificationDefinitions.NOTIFICATIONS_PER_MED) {
                for (i in idx + 1 until horario.size) {
                    if (createdNotifications++ >= NotificationDefinitions.NOTIFICATIONS_PER_MED) break

                    notificationManager.scheduleNotification(med, calendar.time.zeroTime(), horario[i])

                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }

                idx = -1
            }
        }
    }

    suspend fun invoke(user: UsuarioItem) {
        pillboxDbRepository.getMedicamentosActivos(user).forEach { invoke(it) }
    }

    suspend fun invoke() {
        pillboxDbRepository.getUsers().forEach { invoke(it) }
    }
}