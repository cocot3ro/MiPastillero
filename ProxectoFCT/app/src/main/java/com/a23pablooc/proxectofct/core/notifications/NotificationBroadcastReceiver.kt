package com.a23pablooc.proxectofct.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.domain.model.NotificacionItem
import com.a23pablooc.proxectofct.ui.view.activity.MainActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onReceive(context: Context, intent: Intent) {
        createChannel(context)

        serviceScope.launch(Dispatchers.IO) {
            if (dataStoreManager.useNotifications().first()) {
                createNotification(context, intent)
            }
        }
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            "CHANNEL_NAME", // TODO: Hardcode string
            "CHANNEL_NAME", // TODO: Hardcode string
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "CHANNEL_DESCRIPTION" // TODO: Hardcode string
        }

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun createNotification(context: Context, intent: Intent) {
        val json = intent.getStringExtra(NotificationDefinitions.NOTIF_KEY)!!
        val noti = gson.fromJson(json, NotificacionItem::class.java)

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val marcarTomaIntent = Intent(context, NotificationService::class.java).apply {
            putExtra(NotificationDefinitions.NOTIF_KEY, json)
        }

        val marcarTomaPendingIntent: PendingIntent = PendingIntent.getService(
            context,
            noti.pkNotificacion,
            marcarTomaIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        //TODO: Hardcode string
        val notification = NotificationCompat.Builder(context, "CHANNEL_NAME")
            .setSmallIcon(R.drawable.pill_16dp)
            .setContentTitle("Hora de tomar tu medicamento")
            .setContentText("Es hora de tomar ${noti.fkMedicamentoActivo.fkMedicamento.nombre}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openAppPendingIntent)
            .addAction(0, "Marcar toma", marcarTomaPendingIntent)
            .setAutoCancel(true)

        if (noti.fkMedicamentoActivo.fkMedicamento.imagen.toString().isNotBlank()) {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(noti.fkMedicamentoActivo.fkMedicamento.imagen)
                .submit()
                .get()

            notification.setLargeIcon(bitmap)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(noti.pkNotificacion, notification.build())
    }
}