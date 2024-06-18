package com.a23pablooc.proxectofct.core

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatShortTime
import java.util.Date
import kotlin.math.floor

object DateTimeUtils {

    val now: Date get() = Calendar.getInstance().time

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
        return floor((endDate.time - startDate.time) / (1000 * 60 * 60 * 24).toDouble()).toInt()
    }

    fun Date.get(value: Int): Int {
        val calendar = Calendar.getInstance().apply {
            time = this@get
        }

        return calendar.get(value)
    }

    fun Date.isToday(): Boolean {
        val today = Calendar.getInstance().apply {
            time = now
        }

        val date = Calendar.getInstance().apply {
            time = this@isToday
        }

        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == date.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)
    }
}