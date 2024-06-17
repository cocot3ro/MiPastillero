package com.a23pablooc.proxectofct.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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

    fun scheduleNotification(med: MedicamentoActivoItem, timeStamp: Date): NotificacionItem {
        val notificationId = generateNotificationId(med, timeStamp)

        val createdNotification = NotificacionItem(
            pkNotificacion = notificationId,
            fkMedicamentoActivo = med,
            fkUsuario = med.fkUsuario,
            timeStamp = timeStamp
        )

        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra(
                NotificationDefinitions.NOTIF_KEY,
                gson.toJson(createdNotification, NotificacionItem::class.java)
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        Log.v("NotificationManager", "")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeStamp.time,
                pendingIntent
            ).also {
                Log.v("NotificationManager", "setExact vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
                Log.v(
                    "NotificationManager",
                    "Scheduled notification for $timeStamp"
                )
                Log.v("NotificationManager", "setExact ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
            }
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                timeStamp.time,
                pendingIntent
            ).also {
                Log.v("NotificationManager", "set vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
                Log.v(
                    "NotificationManager",
                    "Scheduled set notification for $timeStamp"
                )
                Log.v("NotificationManager", "set ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
            }
        }

        return createdNotification
    }

    private fun generateNotificationId(med: MedicamentoActivoItem, timeStamp: Date): Int {
        return (med.pkMedicamentoActivo + timeStamp.time).hashCode()
    }

    fun cancelNotification(med: MedicamentoActivoItem, timeStamp: Date) {
        val notificationId = generateNotificationId(med, timeStamp)
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        Log.v("NotificationManager", "")
        Log.v("NotificationManager", "cancelNotification vvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
        Log.v(
            "NotificationManager",
            "Cancelling notification for $timeStamp"
        )
        Log.v("NotificationManager", "cancelNotification ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")

        alarmManager.cancel(pendingIntent)
    }
}