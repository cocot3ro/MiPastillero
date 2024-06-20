package com.a23pablooc.proxectofct.data.model.typeadapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.Date

class DateTypeAdapter : TypeAdapter<Date>() {
    override fun write(out: JsonWriter?, value: Date?) {
        out?.value(value?.time)
    }

    override fun read(`in`: JsonReader?): Date {
        return Date(`in`?.nextLong() ?: 0)
    }

}