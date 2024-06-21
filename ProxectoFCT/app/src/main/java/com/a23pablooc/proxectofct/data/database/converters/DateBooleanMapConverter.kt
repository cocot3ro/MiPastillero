package com.a23pablooc.proxectofct.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

object DateBooleanMapConverter {
    private val gson = Gson()

    @TypeConverter
    fun mapToJson(map: MutableMap<Date, Boolean>): String {
        val dateLongMap = map.mapKeys { DateConverter.dateToLong(it.key) }

        return gson.toJson(dateLongMap)
    }

    @TypeConverter
    fun jsonToMap(json: String): MutableMap<Date, Boolean> {
        val longBooleanMap: MutableMap<Long, Boolean> =
            gson.fromJson(json, object : TypeToken<MutableMap<Long, Boolean>>() {}.type)

        return longBooleanMap.mapKeys { DateConverter.longToDate(it.key) }.toMutableMap()
    }
}