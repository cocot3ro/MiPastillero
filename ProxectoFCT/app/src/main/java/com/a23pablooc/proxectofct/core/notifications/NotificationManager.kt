package com.a23pablooc.proxectofct.core.notifications

import com.a23pablooc.proxectofct.core.DataStoreManager
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import java.util.Date
import javax.inject.Inject

class NotificationManager @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {

    fun scheduleNotification(med: MedicamentoActivoItem, dia: Date, hora: Date) {

    }
}