package com.cocot3ro.mipastillero.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.Date

object DateSetConverter {
    private val gson = Gson()

    @TypeConverter
    fun dateSetToJson(set: MutableSet<Date>): String {
        return gson.toJson(set.map { DateConverter.dateToLong(it) })
    }

    @TypeConverter
    fun jsonToDateSet(json: String): MutableSet<Date> {
        return gson.fromJson(json, Array<Long>::class.java)
            .map { DateConverter.longToDate(it) }
            .toMutableSet()
    }
}