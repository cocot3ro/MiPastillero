package com.example.uf1_proyecto

import android.content.Context
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.MutableLiveData

class CalendarViewModel : ViewModel() {

    fun getEnumByString(s: String, contexto: Context): CalendarViewMode? {
        return CalendarViewMode.getByString(s, contexto)
    }

    fun getEnumAsString(calendarViewMode: CalendarViewMode, contexto: Context): String {
        return CalendarViewMode.getAsString(calendarViewMode, contexto)
    }

    // TODO: fun get this week

    // TODO: fun millis to day

    // TODO: fun millis to hour


}