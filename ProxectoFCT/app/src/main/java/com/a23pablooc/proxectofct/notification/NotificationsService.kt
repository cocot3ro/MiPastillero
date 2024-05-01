package com.a23pablooc.proxectofct.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.model.Medicamento
import com.a23pablooc.proxectofct.utils.DateTimeUtils
import com.a23pablooc.proxectofct.utils.PreferencesUtils
import com.a23pablooc.proxectofct.view.MainActivity
import java.util.Calendar
import java.util.Objects

class NotificationsService : BroadcastReceiver() {

    override fun onReceive(context: Context, i: Intent?) {
        createChannel(context)

        if (isNotificationEnabled(context)) {
            createSimpleNotification(context, i!!)
        }
    }

    private fun createSimpleNotification(context: Context, i: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val nombreMedicamento = i.getStringExtra(ContratoNotificaciones.NOMBRE_MEDICAMENTO)
        val imgMedicamento = i.getByteArrayExtra(ContratoNotificaciones.IMG_MEDICAMENTO)

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, ContratoNotificaciones.CHANNEL_ID_NAME)
            .setSmallIcon(R.mipmap.pildora)
            .setContentTitle("Recordatorio")
            .setContentText("${context.getString(R.string.tienes_que_tomar)} $nombreMedicamento")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (imgMedicamento != null && imgMedicamento.isNotEmpty()) {
            notification.setLargeIcon(
                BitmapFactory.decodeByteArray(
                    imgMedicamento,
                    0,
                    imgMedicamento.size
                )
            )
        }

        val notificationID = i.getIntExtra(ContratoNotificaciones.NOTIFICATION_ID, -1)

        notificationManager.cancel(notificationID)

        val actionIntent = Intent(context, NotificationAction::class.java)
        actionIntent.putExtra(ContratoNotificaciones.NOMBRE_MEDICAMENTO, nombreMedicamento)
        actionIntent.putExtra(ContratoNotificaciones.HORA, i.getLongExtra(ContratoNotificaciones.HORA, 0))
        actionIntent.putExtra(ContratoNotificaciones.DIA, i.getLongExtra(ContratoNotificaciones.DIA, 0))

        val actionPendingIntent = PendingIntent.getService(
            context,
            0,
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notification.addAction(
            android.R.drawable.checkbox_on_background,
            context.getString(R.string.marcar_como_tomado),
            actionPendingIntent
        )

        notificationManager.notify(intent.getIntExtra(ContratoNotificaciones.NOTIFICATION_ID, -1), notification.build())
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            ContratoNotificaciones.CHANNEL_ID_NAME,
            ContratoNotificaciones.CHANNEL_ID_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = ContratoNotificaciones.CHANNEL_DESCRIPTION
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun programarNotificacion(context: Context, medicamento: Medicamento) {
        var startDate = medicamento.fechaInicio!!
        val endDate = medicamento.fechaFin!!

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        while (startDate <= endDate) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = startDate

            for (hora: Long in medicamento.horario!!) {
                calendar.set(Calendar.HOUR_OF_DAY, DateTimeUtils.hourFromMillis(hora))
                calendar.set(Calendar.MINUTE, DateTimeUtils.minuteFromMillis(hora))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                if (calendar.timeInMillis < System.currentTimeMillis()) {
                    continue
                }

                val hashCode: Int = Objects.hash(medicamento, startDate, hora)

                val intent =
                    Intent(context, NotificationsService::class.java)
                intent.putExtra(ContratoNotificaciones.NOTIFICATION_ID, hashCode)
                intent.putExtra(ContratoNotificaciones.NOMBRE_MEDICAMENTO, medicamento.nombre!!)
                intent.putExtra(ContratoNotificaciones.IMG_MEDICAMENTO, medicamento.imagen!!)
                intent.putExtra(ContratoNotificaciones.HORA, hora)
                intent.putExtra(ContratoNotificaciones.DIA, startDate)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    hashCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }

            startDate += DateTimeUtils.MILLIS_IN_DAY
        }
    }

    fun cancelarNotificacion(context: Context, medicamento: Medicamento) {
        var startDate = medicamento.fechaInicio!!
        val endDate = medicamento.fechaFin!!

        while (startDate <= endDate) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = startDate

            for (hora: Long in medicamento.horario!!) {
                calendar.set(Calendar.HOUR_OF_DAY, DateTimeUtils.hourFromMillis(hora))
                calendar.set(Calendar.MINUTE, DateTimeUtils.minuteFromMillis(hora))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val hashCode: Int = Objects.hash(medicamento, startDate, hora)

                val intent = Intent(context, NotificationsService::class.java)
                intent.putExtra(ContratoNotificaciones.NOTIFICATION_ID, hashCode)

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    hashCode,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )

                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)

            }

            startDate += DateTimeUtils.MILLIS_IN_DAY
        }
    }

    private fun isNotificationEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PreferencesUtils.PREFS_NAME, AppCompatActivity.MODE_PRIVATE)
            .getBoolean(PreferencesUtils.KEYS.NOTIFICATIONS, true)
    }
}