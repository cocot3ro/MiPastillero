package com.a23pablooc.proxectofct.data.database.converters

import androidx.room.TypeConverter
import java.util.Calendar
import java.util.Date

object DateConverter {
    @TypeConverter
    fun dateToLong(date: Date): Long {
        return Calendar.getInstance().apply {
            time = date
        }.timeInMillis
    }

    @TypeConverter
    fun longToDate(long: Long): Date {
        return Calendar.getInstance().apply {
            timeInMillis = long
        }.time
    }
}