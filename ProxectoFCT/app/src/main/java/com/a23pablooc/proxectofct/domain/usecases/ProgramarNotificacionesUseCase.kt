package com.a23pablooc.proxectofct.domain.usecases

import android.icu.util.Calendar
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.notifications.NotificationDefinitions
import com.a23pablooc.proxectofct.core.notifications.NotificationManager
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.NotificacionItem
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
import java.util.Date
import javax.inject.Inject

class ProgramarNotificacionesUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository,
    private val notificationManager: NotificationManager
) {
    private suspend fun invoke(med: MedicamentoActivoItem) {
        val horario: List<Date> = med.horario.sorted().toList()
        val notifications: List<NotificacionItem> = pillboxDbRepository.getNotificaciones(med)

        val now = DateTimeUtils.today
        val notificacionesFuturas =
            notifications.filter { it.fecha.after(now) || (it.fecha == now && it.hora.after(now)) }
        val notificacionesAEliminar = notifications - notificacionesFuturas.toSet()

        notificacionesAEliminar.forEach {
            notificationManager.cancelNotification(med, it.fecha, it.hora)
            pillboxDbRepository.deleteNotificacion(it)
        }

        if (notificacionesFuturas.size >= NotificationDefinitions.MAX_NOTIFICATIONS) return

        var notificacionesAProgramar =
            NotificationDefinitions.MAX_NOTIFICATIONS - notificacionesFuturas.size
        val calendar = Calendar.getInstance().apply { time = med.fechaInicio }

        while (notificacionesAProgramar > 0 && calendar.time.before(med.fechaFin)) {
            for (hora in horario) {
                if (notificacionesAProgramar <= 0) break

                if (calendar.time.after(now) || (calendar.time == now && hora.after(now))) {
                    val notificacion =
                        notificationManager.scheduleNotification(med, calendar.time, hora)
                    pillboxDbRepository.insertNotificacion(notificacion)
                    notificacionesAProgramar--
                }
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private suspend fun invoke(user: UsuarioItem) {
        val medicamentosActivos = pillboxDbRepository.getMedicamentosActivos(user)
        medicamentosActivos.forEach { invoke(it) }
    }

    suspend fun invoke() {
        val users = pillboxDbRepository.getUsers()
        users.forEach { invoke(it) }
    }
}