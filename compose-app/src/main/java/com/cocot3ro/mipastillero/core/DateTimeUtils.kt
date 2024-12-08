package com.cocot3ro.mipastillero.core

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.cocot3ro.mipastillero.R
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

object DateTimeUtils {

    val now: Date get() = Calendar.getInstance().time

    fun Date.getDayName(context: Context): String {
        val dayOfWeek = Calendar.getInstance().apply {
            time = this@getDayName
        }.get(Calendar.DAY_OF_WEEK)

        return if (dayOfWeek in Calendar.SUNDAY..Calendar.SATURDAY)
            context.resources.getStringArray(R.array.days)[dayOfWeek - 1]
        else
            throw RuntimeException("Invalid day: $dayOfWeek")
    }

    fun Date.formatDate(): String {
        return SimpleDateFormat.getDateInstance().format(this)
    }

    fun parseDate(date: String): Date {
        return SimpleDateFormat.getDateInstance().parse(date)!!
    }

    fun Date.formatShortTime(): String {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(this)
    }

    fun Date.formatTime(): String {
        return SimpleDateFormat.getTimeInstance().format(this)
    }

    fun parseTime(time: String): Date {
        return SimpleDateFormat.getTimeInstance().parse(time)!!
    }

    fun daysBetweenDates(startDate: Date, endDate: Date): Int {
        val startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        return ChronoUnit.DAYS.between(startLocalDate, endLocalDate).toInt()
    }

    fun Date.get(value: Int): Int {
        val calendar = Calendar.getInstance().apply {
            time = this@get
        }

        return calendar.get(value)
    }

    fun Date.isToday(): Boolean {
        val today = now

        val date = Calendar.getInstance().apply {
            time = this@isToday
        }

        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
    }
}