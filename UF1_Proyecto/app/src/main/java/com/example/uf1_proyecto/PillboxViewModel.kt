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


    // TODO: fun day to millis
    fun millisToDate(milisegundos: Long): String {
        val formatoFecha = SimpleDateFormat.getDateInstance()
        val fecha = Date(milisegundos)
        println("$milisegundos ms -> ${formatoFecha.format(fecha)}")
        return formatoFecha.format(fecha)
    }

    // TODO: fun dateToMillis
    // TODO: fun millisToHour
    // TODO: fun hourToMillis


}
