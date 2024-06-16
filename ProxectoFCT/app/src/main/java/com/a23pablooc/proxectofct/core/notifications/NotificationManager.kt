package com.a23pablooc.proxectofct.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import com.a23pablooc.proxectofct.core.DateTimeUtils.get
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.domain.model.NotificacionItem
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(med: MedicamentoActivoItem, dia: Date, hora: Date): NotificacionItem {
        val notificationId = generateNotificationId(med, dia, hora)

        val createdNotification = NotificacionItem(
            pkNotificacion = notificationId,
            fkMedicamentoActivo = med,
            fkUsuario = med.fkUsuario,
            fecha = dia,
            hora = hora
        )

        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra(NotificationDefinitions.NOTIF_KEY, gson.toJson(createdNotification, NotificacionItem::class.java))
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calculateTriggerTime(dia, hora),
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calculateTriggerTime(dia, hora),
                pendingIntent
            )
        }

        return createdNotification
    }

    private fun generateNotificationId(med: MedicamentoActivoItem, dia: Date, hora: Date): Int {
        return (med.pkMedicamentoActivo + dia.time + hora.time).hashCode()
    }

    private fun calculateTriggerTime(dia: Date, hora: Date): Long {
        val calendar = Calendar.getInstance().apply {
            time = dia
            set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, hora.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun cancelNotification(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        val notificationId = generateNotificationId(med, dia, hora)
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}