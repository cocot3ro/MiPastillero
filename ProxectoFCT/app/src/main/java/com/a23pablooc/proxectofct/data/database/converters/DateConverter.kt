package com.a23pablooc.proxectofct.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
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

    @TypeConverter
    fun dateSetToJson(set: Set<Date>): String {
        return Gson().toJson(set.map { dateToLong(it) })
    }

    @TypeConverter
    fun jsonToDateSet(json: String): Set<Date> {
        return Gson().fromJson(json, Array<Long>::class.java).map { longToDate(it) }.toSet()
    }
}