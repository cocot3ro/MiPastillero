package com.a23pablooc.proxectofct.core

import android.content.Context
import android.icu.text.SimpleDateFormat
import com.a23pablooc.proxectofct.R
import java.util.Calendar
import java.util.Date

object DateTimeUtils {

    fun Date.getDayName(context: Context): String {
        val dayOfWeek = Calendar.getInstance().apply {
            time = this@getDayName
        }.get(Calendar.DAY_OF_WEEK)

        return if (dayOfWeek in Calendar.SUNDAY..Calendar.SATURDAY)
            context.resources.getStringArray(R.array.dias_semana)[dayOfWeek - 1]
        else
            throw RuntimeException("Invalid day: $dayOfWeek")
    }

    fun Date.formatDate(): String {
        return SimpleDateFormat.getDateInstance().format(this)
    }

    fun Date.formatTime(): String {
        return SimpleDateFormat.getTimeInstance().format(this)
    }

    fun daysBetweenDates(startDate: Date, endDate: Date): Int {
        return ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt()
    }

    fun Date.zero() = apply {
        zeroTime()
        zeroDate()
    }

    fun Date.zeroTime() = apply {
        this.time = Calendar.getInstance().apply {
            time = this@zeroTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun Date.zeroDate() = apply {
        this.time = Calendar.getInstance().apply {
            time = this@zeroDate
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, 0)
            set(Calendar.YEAR, 0)
        }.timeInMillis
    }

    fun parseDate(date: String): Date {
        return SimpleDateFormat.getDateInstance().parse(date)!!
    }

    fun parseTime(time: String): Date {
        return SimpleDateFormat.getTimeInstance().parse(time)!!
    }
}