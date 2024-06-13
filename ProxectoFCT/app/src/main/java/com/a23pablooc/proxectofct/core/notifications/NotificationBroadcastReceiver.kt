package com.a23pablooc.proxectofct.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class NotificationBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var gson: Gson

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onReceive(context: Context?, intent: Intent?) {
        
    }
}