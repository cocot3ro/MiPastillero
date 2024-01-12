package com.example.uf1_proyecto.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.uf1_proyecto.R
import com.example.uf1_proyecto.model.Medicamento
import com.example.uf1_proyecto.utils.PreferencesUtils
import com.example.uf1_proyecto.view.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar

class NotificationManager : BroadcastReceiver() {
    private companion object {
        const val CHANNEL_ID_NAME = "Recordatorios"
        const val CHANNEL_DESCRIPTION =
            "Canal de recordatorios. Recibirás notificaciones relacionadas con la toma de medicamentos"
        const val NOTIFICATION_ID = "notify_id"
        const val NOMBRE_MEDICAMENTO = "nombre_medicamento"
        const val IMG_MEDICAMENTO = "img_medicamento"
    }

    private var _preferences: SharedPreferences? = null
    private var _alarmManager: AlarmManager? = null
    private var _notificationManager: NotificationManager? = null

    private val preferences get() = _preferences!!
    private val alarmManager get() = _alarmManager!!
    private val notificationManager get() = _notificationManager!!

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("NotificationManager", "onReceive")
        createChannel(context)

        if (isNotificationEnabled(context)) {
            createSimpleNotification(context, intent!!)
        }
    }

    private fun initVars(context: Context, from: String) {
        Log.d("NotificationManager", "initVars -> $from")
        _preferences = _preferences ?: context.getSharedPreferences(PreferencesUtils.PREFS_NAME, 0)
        _alarmManager =
            _alarmManager ?: context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        _notificationManager = _notificationManager
            ?: context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun createSimpleNotification(context: Context, i: Intent) {
        Log.d("NotificationManager", "createSimpleNotification")
        initVars(context, "createSimpleNotification")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val nombreMedicamento = i.getStringExtra(NOMBRE_MEDICAMENTO).also { Log.d("NotificationManager", "Nombre medicamento = $it") }
        val imgMedicamento = i.getByteArrayExtra(IMG_MEDICAMENTO).also { Log.d("NotificationManager", "Imagen medicamento = $it") }

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_NAME)
            .setSmallIcon(R.mipmap.app_image)
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

        notificationManager.notify(intent.getIntExtra(NOTIFICATION_ID, -1), notification.build())
    }

    private fun createChannel(context: Context) {
        Log.d("NotificationManager", "createChannel")
        initVars(context, "createChannel")

        val channel = NotificationChannel(
            CHANNEL_ID_NAME,
            CHANNEL_ID_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }

        notificationManager.createNotificationChannel(channel)
    }

    fun programarNotificacion(context: Context, medicamento: Medicamento) {
        initVars(context, "programarNotificacion")

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(NOTIFICATION_ID, medicamento.hashCode())
        intent.putExtra(NOMBRE_MEDICAMENTO, medicamento.nombre!!)
        intent.putExtra(IMG_MEDICAMENTO, medicamento.imagen!!)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicamento.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

//        var startDate = medicamento.fechaInicio!!
//        val endDate = medicamento.fechaFin!!
//
//        while (startDate <= endDate) {
//            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = startDate
//
//            for (hora: Long in medicamento.horario!!) {
//                calendar.set(Calendar.HOUR_OF_DAY, DateTimeUtils.getHour(hora))
//                calendar.set(Calendar.MINUTE, DateTimeUtils.getMinute(hora))
//                calendar.set(Calendar.SECOND, 0)
//                calendar.set(Calendar.MILLISECOND, 0)
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
//                    alarmManager.setExact(
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.timeInMillis,
//                        pendingIntent
//                    )
//                } else {
//                    alarmManager.set(
//                        AlarmManager.RTC_WAKEUP,
//                        calendar.timeInMillis,
//                        pendingIntent
//                    )
//                }
//            }
//
//            startDate += DateTimeUtils.MILLIS_IN_DAY
//        }

        val calendar = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                (calendar.timeInMillis + 5000).also { Log.d("NotificationManager", "Alarma programada para ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", context.resources.configuration.locales[0]).format(it)}") },
                pendingIntent
            )
        } else {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                (calendar.timeInMillis + 5000).also { Log.d("NotificationManager", "Alarma programada para ${SimpleDateFormat("dd/MM/yyyy HH:mm:ss", context.resources.configuration.locales[0]).format(it)}") },
                pendingIntent
            )
        }
    }

    fun cancelarNotificacion(context: Context, medicamento: Medicamento) {
        initVars(context, "cancelarNotificacion")

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(NOTIFICATION_ID, medicamento.hashCode())

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicamento.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    private fun isNotificationEnabled(context: Context): Boolean {
        initVars(context, "isNotificationEnabled")

        return preferences.getBoolean(PreferencesUtils.KEYS.NOTIFICATIONS, true)
            .also { Log.d("NotificationManager", "Notifications enabled = $it") }
    }
}