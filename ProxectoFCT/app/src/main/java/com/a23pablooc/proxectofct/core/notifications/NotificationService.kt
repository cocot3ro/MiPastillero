package com.a23pablooc.proxectofct.core.notifications

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.a23pablooc.proxectofct.domain.model.NotificacionItem
import com.a23pablooc.proxectofct.domain.usecases.MarcarTomaUseCase
import com.a23pablooc.proxectofct.domain.usecases.ProgramarNotificacionesUseCase
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : Service() {

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var marcarTomaUseCase: MarcarTomaUseCase

    @Inject
    lateinit var programarNotificacionesUseCase: ProgramarNotificacionesUseCase

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val json = intent.getStringExtra(NotificationDefinitions.NOTIF_KEY)!!

        val noti = gson.fromJson(json, NotificacionItem::class.java)

        serviceScope.launch(Dispatchers.IO) {
            marcarTomaUseCase.invoke(noti.fkMedicamentoActivo, noti.fecha, noti.hora, true)
            programarNotificacionesUseCase.invoke()
        }

        stop(intent)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun stop(intent: Intent) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(intent.getIntExtra(NotificationDefinitions.NOTIF_KEY, -1))

        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}