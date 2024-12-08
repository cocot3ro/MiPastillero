package com.cocot3ro.mipastillero.data.database.converters

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(long: Long): Date {
        return Date(long)
    }
}