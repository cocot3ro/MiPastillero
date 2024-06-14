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
            NotificationDefinitions.CHANNEL_NAME,
            NotificationDefinitions.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = NotificationDefinitions.CHANNEL_DESCRIPTION
        }

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun createNotification(context: Context, i: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val noti = gson.fromJson(
            i.getStringExtra(NotificationDefinitions.NOTI)!!,
            NotificacionItem::class.java
        )

        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(
            context,
            NotificationDefinitions.CHANNEL_NAME
        )

        notificationBuilder.setSmallIcon(R.drawable.pill_32dp)
            .setContentTitle("Recordatorio")
            .setContentText("${context.getString(R.string.tienes_que_tomar)} ${noti.fkMedicamentoActivo.fkMedicamento.nombre}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (noti.fkMedicamentoActivo.fkMedicamento.imagen.toString().isNotBlank()) {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(noti.fkMedicamentoActivo.fkMedicamento.imagen)
                .submit()
                .get()

            notificationBuilder.setLargeIcon(bitmap)
        }

        notificationManager.cancel(noti.pkNotificacion)

        val actionIntent = Intent(context, NotificationService::class.java).apply {
            putExtra(NotificationDefinitions.NOTI, gson.toJson(noti))
            putExtra(NotificationDefinitions.HOUR, i.getLongExtra(NotificationDefinitions.HOUR, 0))
            putExtra(NotificationDefinitions.DAY, i.getLongExtra(NotificationDefinitions.DAY, 0))
        }
    }
}