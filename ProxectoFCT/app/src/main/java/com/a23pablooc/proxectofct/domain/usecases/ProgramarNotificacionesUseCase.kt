package com.a23pablooc.proxectofct.domain.usecases

import android.icu.util.Calendar
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.get
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
        val calendar = Calendar.getInstance().apply { time = DateTimeUtils.today.zeroTime() }
        val alreadyScheduledNotifications = pillboxDbRepository.getNotificaciones(med)

        if (alreadyScheduledNotifications.size == NotificationDefinitions.NOTIFICATIONS_PER_MED) return

        val startDate = med.fechaInicio
        val endDate = med.fechaFin
        val hours = med.horario.toList()

        var remainingNotifications = NotificationDefinitions.NOTIFICATIONS_PER_MED - alreadyScheduledNotifications.size

        // Schedule notifications until the end date or until the maximum number of notifications is reached

    }

    suspend fun invoke(usuario: UsuarioItem) {

    }
}
