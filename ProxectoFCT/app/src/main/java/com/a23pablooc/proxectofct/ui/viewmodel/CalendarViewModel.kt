package com.a23pablooc.proxectofct.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.core.UserInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    val userInfoProvider: UserInfoProvider
) : ViewModel() {

    fun calculateOffset(year: Int, monthOfYear: Int, dayOfMonth: Int): Int {
        val date = Calendar.getInstance().apply {
            time = DateTimeUtils.today.zeroTime()
            set(year, monthOfYear, dayOfMonth)
        }.time

        val today = DateTimeUtils.today.zeroTime()

        return DateTimeUtils.daysBetweenDates(today, date)
    }
}