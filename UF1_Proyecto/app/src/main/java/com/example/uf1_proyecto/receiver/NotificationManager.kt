package com.example.uf1_proyecto.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toIcon
import com.example.uf1_proyecto.R
import com.example.uf1_proyecto.model.Medicamento
import com.example.uf1_proyecto.utils.DateTimeUtils
import com.example.uf1_proyecto.utils.PreferencesUtils
import java.util.Calendar

class NotificationManager(private val context: Context) : BroadcastReceiver() {
    private companion object {
        const val CHANNEL_NAME = "Recordatorios"
        const val CHANNEL_DESCRIPTION =
            "Canal de recordatorios. Recibirás notificaciones relacionadas con la toma de medicamentos"
        const val NOTIFICATION_ID = "notify_id"
        const val NOMBRE_MEDICAMENTO = "nombre_medicamento"
        const val IMG_MEDICAMENTO = "img_medicamento"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PreferencesUtils.PREFS_NAME, 0)

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    init {
        createChannel()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (isNotificationEnabled()) {
            createSimpleNotification()
        }
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_NAME,
            CHANNEL_NAME,
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun createSimpleNotification() {
        val intent = Intent(context, NotificationManager::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val nombreMedicamento = intent.getStringExtra(NOMBRE_MEDICAMENTO)
        val imgMedicamento = intent.getByteArrayExtra(IMG_MEDICAMENTO)

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        //TODO:
        //TODO: botón en la notificación para marcar como tomado

        val notification = NotificationCompat.Builder(context, CHANNEL_NAME)
            .setSmallIcon(R.mipmap.app_image)
            .setContentTitle("Recordatorio")
            //TODO: traducir string
            .setContentText("${context.getString(R.string.tienes_que_tomar)} $nombreMedicamento")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

//        if (imgMedicamento != null && imgMedicamento.isNotEmpty()) {
//            notification.setLargeIcon(BitmapFactory.decodeByteArray(imgMedicamento, 0, imgMedicamento.size))
//        }
    }

    fun programarNotificacion(medicamento: Medicamento) {
        val intent = Intent(context, NotificationManager::class.java)
        intent.putExtra(NOTIFICATION_ID, medicamento.hashCode())
        intent.putExtra(NOMBRE_MEDICAMENTO, medicamento.nombre!!)
        intent.putExtra(IMG_MEDICAMENTO, medicamento.imagen!!)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicamento.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        var startDate = medicamento.fechaInicio!!
        val endDate = medicamento.fechaFin!!

        while (startDate <= endDate) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = startDate

            for (hora: Long in medicamento.horario!!) {
                calendar.set(Calendar.HOUR_OF_DAY, DateTimeUtils.getHour(hora))
                calendar.set(Calendar.MINUTE, DateTimeUtils.getMinute(hora))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

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

    fun cancelarNotificacion(medicamento: Medicamento) {
        val intent = Intent(context, NotificationManager::class.java)
        intent.putExtra(NOTIFICATION_ID, medicamento.hashCode())

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicamento.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    private fun isNotificationEnabled(): Boolean =
        preferences.getBoolean(PreferencesUtils.KEYS.NOTIFICATIONS, true)

}