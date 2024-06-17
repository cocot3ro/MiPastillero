package com.a23pablooc.proxectofct.domain.usecases

import android.icu.util.Calendar
import android.util.Log
import com.a23pablooc.proxectofct.core.DateTimeUtils.get
import com.a23pablooc.proxectofct.core.DateTimeUtils.now
import com.a23pablooc.proxectofct.core.notifications.NotificationDefinitions.MAX_NOTIFICATIONS
import com.a23pablooc.proxectofct.core.notifications.NotificationManager
import com.a23pablooc.proxectofct.data.repositories.PillboxDbRepository
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.UsuarioItem
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

        val notificacionesFuturas = notifications.filter { it.timeStamp >= now }
        val notificacionesAEliminar = notifications - notificacionesFuturas.toSet()

        Log.v("ProgramarNotificacionesUseCase", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
        Log.v("ProgramarNotificacionesUseCase", "notifications:${System.lineSeparator()}${
            notifications.joinToString(
                separator = System.lineSeparator()
            ) { "${it.pkNotificacion} ${it.timeStamp} ${it.fkMedicamentoActivo.pkMedicamentoActivo}" }
        }")
        Log.v("ProgramarNotificacionesUseCase", "notificacionesFuturas:${System.lineSeparator()}${
            notificacionesFuturas.joinToString(
                separator = System.lineSeparator()
            ) { "${it.pkNotificacion} ${it.timeStamp} ${it.fkMedicamentoActivo.pkMedicamentoActivo}" }
        }")
        Log.v("ProgramarNotificacionesUseCase", "notificacionesAEliminar:${System.lineSeparator()}${
            notificacionesAEliminar.joinToString(
                separator = System.lineSeparator()
            ) { "${it.pkNotificacion} ${it.timeStamp} ${it.fkMedicamentoActivo.pkMedicamentoActivo}" }
        }")
        Log.v("ProgramarNotificacionesUseCase", "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")

        notificacionesAEliminar.forEach {
            notificationManager.cancelNotification(med, it.timeStamp)
            pillboxDbRepository.deleteNotificacion(it)
        }

        if (notificacionesFuturas.size >= MAX_NOTIFICATIONS) return

        var notificacionesAProgramar = MAX_NOTIFICATIONS - notificacionesFuturas.size
        val calendar = Calendar.getInstance().apply {
            time = notificacionesFuturas.lastOrNull()?.timeStamp ?: med.fechaInicio
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        while (notificacionesAProgramar > 0 && calendar.time <= med.fechaFin) {
            for (hora in horario) {
                if (notificacionesAProgramar <= 0) break

                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY))
                    set(Calendar.MINUTE, hora.get(Calendar.MINUTE))
                }

                if (calendar.time >= now) {
                    val notificacion =
                        notificationManager.scheduleNotification(med, calendar.time)

//                    pillboxDbRepository.insertNotificacion(notificacion)

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