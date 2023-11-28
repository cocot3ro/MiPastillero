package com.example.uf1_proyecto

import android.content.Context

enum class Days {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    companion object {
        fun getByString(s: String, contexto: Context): Days? {
            return when (s) {
                contexto.getString(R.string.monday) -> MONDAY
                contexto.getString(R.string.tuesday) -> TUESDAY
                contexto.getString(R.string.wednesday) -> WEDNESDAY
                contexto.getString(R.string.thursday) -> THURSDAY
                contexto.getString(R.string.friday) -> FRIDAY
                contexto.getString(R.string.saturday) -> SATURDAY
                contexto.getString(R.string.sunday) -> SUNDAY
                else -> null
            }
        }

        fun getAsString(day: Days, contexto: Context): String {
            return when (day) {
                MONDAY -> contexto.getString(R.string.monday)
                TUESDAY -> contexto.getString(R.string.tuesday)
                WEDNESDAY -> contexto.getString(R.string.wednesday)
                THURSDAY -> contexto.getString(R.string.thursday)
                FRIDAY -> contexto.getString(R.string.friday)
                SATURDAY -> contexto.getString(R.string.saturday)
                SUNDAY -> contexto.getString(R.string.sunday)
            }
        }
    }
}