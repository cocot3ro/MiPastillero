package com.cocot3ro.mipastillero.domain.usecases

import android.icu.util.Calendar
import com.cocot3ro.mipastillero.core.DateTimeUtils.get
import com.cocot3ro.mipastillero.core.DateTimeUtils.now
import com.cocot3ro.mipastillero.core.notifications.NotificationDefinitions.MAX_NOTIFICATIONS
import com.cocot3ro.mipastillero.core.notifications.NotificationManager
import com.cocot3ro.mipastillero.data.repositories.PillboxDbRepository
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import javax.inject.Inject

class ProgramarNotificacionesUseCase @Inject constructor(
    private val pillboxDbRepository: PillboxDbRepository,
    private val notificationManager: NotificationManager
) {
    suspend fun invoke() {
        val users = pillboxDbRepository.getUsers()
        users.forEach { invoke(it) }
    }

    suspend fun invoke(user: UsuarioItem) {
        val medicamentosActivos = pillboxDbRepository.getMedicamentosActivos(user)
        medicamentosActivos.forEach { invoke(it) }
    }

    suspend fun invoke(med: MedicamentoActivoItem) {
        val horario = med.horario.sorted().toList()

        val notifications = pillboxDbRepository.getNotificaciones(med.fkUsuario, med)

        val now = now

        val notificacionesFuturas = notifications.filter { it.timeStamp >= now }
        val notificacionesAEliminar = notifications - notificacionesFuturas.toSet()

        notificacionesAEliminar.forEach {
            notificationManager.cancelNotification(med, it.timeStamp)
            pillboxDbRepository.deleteNotificacion(it)
        }

        if (notificacionesFuturas.size >= MAX_NOTIFICATIONS) return

        var notificacionesAProgramar = MAX_NOTIFICATIONS - notificacionesFuturas.size

        val last = notificacionesFuturas.lastOrNull()

        val calendar = Calendar.getInstance().apply {
            if (last == null) {
                time = med.fechaInicio
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            } else {
                time = last.timeStamp
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        val flag = calendar.time

        while (notificacionesAProgramar > 0 && calendar.time <= med.fechaFin) {
            for (hora in horario) {
                if (notificacionesAProgramar <= 0) break

                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, hora.get(Calendar.MINUTE))
                }

                if (calendar.time >= flag && calendar.time > now) {
                    val notificacion =
                        notificationManager.scheduleNotification(med, calendar.time)

                    pillboxDbRepository.insertNotificacion(notificacion)

                    notificacionesAProgramar--
                }
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }
}