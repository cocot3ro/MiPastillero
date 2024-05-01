package com.a23pablooc.proxectofct.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.Date

object SetConverter {
    @TypeConverter
    fun dateSetToJson(set: Set<Date>): String {
        return Gson().toJson(set.map { it.time })
    }

    @TypeConverter
    fun jsonToDateSet(json: String): Set<Date> {
        return Gson().fromJson(json, Array<Long>::class.java).map { Date(it) }.toSet()
    }

    @TypeConverter
    fun longSetToJson(set: Set<Long>): String {
        return Gson().toJson(set)
    }

    @TypeConverter
    fun jsonToLongSet(json: String): Set<Long> {
        return Gson().fromJson(json, Array<Long>::class.java).toSet()
    }
}