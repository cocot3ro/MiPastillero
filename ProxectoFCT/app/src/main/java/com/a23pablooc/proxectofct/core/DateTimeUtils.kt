package com.a23pablooc.proxectofct.core

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.a23pablooc.proxectofct.R
import java.util.Date
import kotlin.math.floor

object DateTimeUtils {

    val today: Date get() = Calendar.getInstance().time
    val zero: Date get() = today.zero()

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

    fun Date.formatTime(): String {
        return SimpleDateFormat.getTimeInstance().format(this)
    }

    fun parseTime(time: String): Date {
        return SimpleDateFormat.getTimeInstance().parse(time)!!
    }

    fun daysBetweenDates(startDate: Date, endDate: Date): Int {
        return floor((endDate.time - startDate.time) / (1000 * 60 * 60 * 24).toDouble()).toInt()
    }

    fun Date.zero() = apply {
        this.zeroTime()
        this.zeroDate()
    }

    fun Date.zeroTime() = apply {
        this.time = Calendar.getInstance().apply {
            timeInMillis = this@zeroTime.time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun Date.zeroDate() = apply {
        this.time = Calendar.getInstance().apply {
            timeInMillis = this@zeroDate.time
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, 0)
            set(Calendar.YEAR, 0)
        }.timeInMillis
    }

    fun Date.get(value: Int): Int {
        val calendar = Calendar.getInstance().apply {
            time = this@get
        }

        return calendar.get(value)
    }
}