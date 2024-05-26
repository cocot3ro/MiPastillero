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

        return when (dayOfWeek) {
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

    fun Date.formatDate(): String {
        return SimpleDateFormat.getDateInstance().format(this)
    }

    fun Date.formatTime(): String {
        return SimpleDateFormat.getTimeInstance().format(this)
    }

    /**
     * Returns the number of days between two dates
     * A negative value indicates that the second date is before the first
     * @param startDate the first date
     * @param endDate the second date
     * @return the number of days between the two dates as an integer
     * @see Date
     */
    fun daysBetweenDates(startDate: Date, endDate: Date): Int {
        return ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt()
    }

    fun Date.zeroTime() {
        val calendar = Calendar.getInstance().apply {
            time = this@zeroTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        this.time = calendar.timeInMillis
    }

    fun Date.zeroDate() {
        val calendar = Calendar.getInstance().apply {
            time = this@zeroDate
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, 0)
            set(Calendar.YEAR, 0)
        }
        this.time = calendar.timeInMillis
    }

    fun Date.zero() {
        zeroTime()
        zeroDate()
    }
}