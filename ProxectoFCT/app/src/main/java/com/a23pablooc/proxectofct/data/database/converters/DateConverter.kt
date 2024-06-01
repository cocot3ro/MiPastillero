package com.a23pablooc.proxectofct.data.database.converters

import android.icu.util.Calendar
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.Date

object DateConverter {
    private val gson = Gson()

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

