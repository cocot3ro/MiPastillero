package com.a23pablooc.proxectofct.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

object DateMapDateBooleanMapConverter {
    private val gson = Gson()

    @TypeConverter
    fun mapToJson(map: MutableMap<Date, MutableMap<Date, Boolean>>): String {
        val dateLongMap = map.mapKeys { DateConverter.dateToLong(it.key) }
            .mapValues { DateBooleanMapConverter.mapToJson(it.value) }

        return gson.toJson(dateLongMap)
    }

    @TypeConverter
    fun jsonToMap(json: String): MutableMap<Date, MutableMap<Date, Boolean>> {
        val longMapJson: MutableMap<Long, String> =
            gson.fromJson(json, object : TypeToken<MutableMap<Long, String>>() {}.type)

        return longMapJson.mapKeys { DateConverter.longToDate(it.key) }
            .mapValues { DateBooleanMapConverter.jsonToMap(it.value) }.toMutableMap()
    }
}