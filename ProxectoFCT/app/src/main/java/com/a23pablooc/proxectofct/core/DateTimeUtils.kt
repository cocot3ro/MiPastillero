package com.a23pablooc.proxectofct.core

import android.content.Context
import android.icu.text.SimpleDateFormat
import com.a23pablooc.proxectofct.R
import java.util.Calendar
import java.util.Date

object DateTimeUtils {

    fun getDayName(context: Context, date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return when (val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> context.getString(R.string.lunes)
            Calendar.TUESDAY -> context.getString(R.string.martes)
            Calendar.WEDNESDAY -> context.getString(R.string.miercoles)
            Calendar.THURSDAY -> context.getString(R.string.jueves)
            Calendar.FRIDAY -> context.getString(R.string.viernes)
            Calendar.SATURDAY -> context.getString(R.string.sabado)
            Calendar.SUNDAY -> context.getString(R.string.domingo)
            else -> throw RuntimeException("Invalid day: $dayOfWeek")
        }
    }

    fun formatDate(date: Date): String {
        return SimpleDateFormat.getDateInstance().format(date)
    }

    fun daysBetweenDates(startDate: Date, endDate: Date): Int {
        return ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt()
    }
}