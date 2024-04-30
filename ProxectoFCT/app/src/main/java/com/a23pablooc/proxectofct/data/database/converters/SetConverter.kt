package com.a23pablooc.proxectofct.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

object SetConverter {
    @TypeConverter
    fun setToJson(set: Set<Long>): String {
        return Gson().toJson(set)
    }

    @TypeConverter
    fun jsonToSet(json: String): Set<Long> {
        return Gson().fromJson(json, Array<Long>::class.java).toSet()
    }
}