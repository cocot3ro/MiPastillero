package com.example.uf1_proyecto

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    var lastPosition: Int
    private var skip: Boolean

    init {
        Log.v("CalendarPagerAdapter", "init")
        _pillboxViewModel = PillboxViewModel.getInstance(fragmentActivity)

        lastPosition = itemCount / 2
        skip = true
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        Log.i("CalendarPagerAdapter", "")

        Log.i("CalendarPagerAdapter", "createFragment: $lastPosition -> $position")

        val data = when {
            position < lastPosition -> pillboxViewModel.getCalendarPrevDayData()
            position > lastPosition -> pillboxViewModel.getCalendarNextDayData()
            else -> pillboxViewModel.getCalendarCurrDayData()
        }

        if (skip) {
            when {
                position < lastPosition -> {
                    pillboxViewModel.calendarMoveBackward().also { Log.i("CalendarPagerAdapter", "skip -> position < lastPosition -> calendarMoveBackward()") }
                    skip = false
                    Log.i(
                        "CalendarPagerAdapter",
                        "createFragment: skipping backward $lastPosition -> $position"
                    )
                }

                position > lastPosition -> {
                    pillboxViewModel.calendarMoveForward().also { Log.i("CalendarPagerAdapter", "skip -> position > lastPosition -> calendarMoveForward()") }
                    skip = false
                    Log.i(
                        "CalendarPagerAdapter",
                        "createFragment: skipping forward $lastPosition -> $position"
                    )
                }

                else -> {
                    Log.i(
                        "CalendarPagerAdapter",
                        "createFragment: skip same $lastPosition -> $position"
                    )
                }
            }
        }

        Log.i(
            "CalendarPagerAdapter",
            "createFragment: $lastPosition -> $position ${DateTimeUtils.millisToDate(data.first)}"
        )

        return CalendarPageFragment(data, position)
    }

}
