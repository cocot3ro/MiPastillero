package com.example.uf1_proyecto.notification

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.uf1_proyecto.viewModel.PillboxViewModel

class NotificationAction : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val pillboxViewModel = PillboxViewModel.getInstance(applicationContext)

        val medName = intent.getStringExtra(ContratoNotificaciones.NOMBRE_MEDICAMENTO)!!
        val hora = intent.getLongExtra(ContratoNotificaciones.HORA, 0)
        val dia = intent.getLongExtra(ContratoNotificaciones.DIA, 0)

        pillboxViewModel.marcarToma(medName, hora, dia)

        stop(intent)

        return START_NOT_STICKY
    }

    private fun stop(intent: Intent) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(intent.getIntExtra(ContratoNotificaciones.NOTIFICATION_ID, -1))

        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
