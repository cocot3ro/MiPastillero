package com.example.uf1_proyecto

import android.content.Context
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import java.util.Date

//import androidx.lifecycle.MutableLiveData

class PillboxViewModel private constructor(context: Context) : ViewModel() {

    private var dbHelper: DBHelper = DBHelper.getInstance(context)

    companion object {
        @Volatile
        private var instance: PillboxViewModel? = null

        fun getInstance(context: Context): PillboxViewModel =
            instance ?: synchronized(this) {
                instance ?: PillboxViewModel(context.applicationContext).also { instance = it }
            }

    }

    // TODO: fun get this week ""
    fun getActives() = dbHelper.getWeek(System.currentTimeMillis())

    fun millisToDate(millis: Long): String =
        SimpleDateFormat.getDateInstance().format(Date(millis))

    fun millisToHour(millis: Long): String =
        SimpleDateFormat.getTimeInstance().format(Date(millis))

    fun hourToMillis(hour: String): Long =
        SimpleDateFormat.getTimeInstance().parse(hour).time

    fun dateToMillis(date: String): Long =
        SimpleDateFormat.getDateInstance().parse(date).time

    fun ejemplosActivos() {
        dbHelper.ejemplosActivos()
    }

}
