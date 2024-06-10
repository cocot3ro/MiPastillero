package com.a23pablooc.proxectofct.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    fun calculateOffset(year: Int, monthOfYear: Int, dayOfMonth: Int): Int {
        val date = Calendar.getInstance().apply {
            set(year, monthOfYear, dayOfMonth)
        }.time.zeroTime()

        val today = Calendar.getInstance().time.zeroTime()

        return DateTimeUtils.daysBetweenDates(today, date)
    }
}