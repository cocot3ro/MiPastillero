package com.example.uf1_proyecto

import android.content.Context

enum class CalendarViewMode {
    DAILY, WEEKLY;

    companion object {
        fun getByString(s: String, contexto: Context): CalendarViewMode? {
            return when (s) {
                contexto.getString(R.string.daily) -> DAILY
                contexto.getString(R.string.weekly) -> WEEKLY
                else -> null
            }
        }

        fun getAsString(calendarViewMode: CalendarViewMode, contexto: Context): String {
            return when (calendarViewMode) {
                DAILY -> contexto.getString(R.string.daily)
                WEEKLY -> contexto.getString(R.string.weekly)
            }
        }
    }
}