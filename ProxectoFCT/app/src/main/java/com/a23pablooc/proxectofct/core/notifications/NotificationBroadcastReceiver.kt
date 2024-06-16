package com.a23pablooc.proxectofct.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.a23pablooc.proxectofct.core.DataStoreManager
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

    private fun createNotification(context: Context, i: Intent) {
        // TODO("Not implemented yet")
    }
}