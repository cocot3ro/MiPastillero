package com.cocot3ro.mipastillero.ui.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.core.UserInfoProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    val userInfoProvider: UserInfoProvider
) : ViewModel() {

    fun calculateOffset(year: Int, monthOfYear: Int, dayOfMonth: Int): Int {
        val date = Calendar.getInstance().apply {
            set(year, monthOfYear, dayOfMonth)
        }.time

        val today = DateTimeUtils.now

        return DateTimeUtils.daysBetweenDates(today, date)
    }
}