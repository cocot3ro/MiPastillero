package com.a23pablooc.proxectofct.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.core.DateTimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import android.icu.util.Calendar
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    fun calculateOffset(year: Int, monthOfYear: Int, dayOfMonth: Int): Int {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, monthOfYear)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }.time

        val today = Calendar.getInstance().time

        return DateTimeUtils.daysBetweenDates(today, date)
    }
}