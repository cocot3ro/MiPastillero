package com.a23pablooc.proxectofct.core.notifications

import android.app.AlarmManager
import android.content.Context
import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject

class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreManager: DataStoreManager
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleNotification(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        TODO("Not yet implemented")
    }

    fun cancelNotification(med: MedicamentoActivoItem, dia: Date, hora: Date) {
        TODO("Not yet implemented")
    }
}