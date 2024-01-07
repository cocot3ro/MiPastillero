package com.example.uf1_proyecto

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.math.abs

class CalendarPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val loggingEnabled: Boolean = false
) :
    FragmentStateAdapter(fragmentActivity) {
    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    var lastPosition: Int

    init {
        if (loggingEnabled) {
            Log.v("CalendarPagerAdapter", "init")
        }
        _pillboxViewModel = PillboxViewModel.getInstance(fragmentActivity)

        lastPosition = itemCount / 2
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        if (loggingEnabled) {
            Log.i("CalendarPagerAdapter", "")
            Log.i("CalendarPagerAdapter", "createFragment: $lastPosition -> $position")
        }

        if (abs(position - lastPosition) > 1) {
            if (loggingEnabled) {
                Log.d(
                    "CalendarPagerAdapter",
                    "createFragment: abs(position - lastPosition) ${abs(position - lastPosition)}"
                )
            }
            for (i in 1 until abs(position - lastPosition)) {
                if (position < lastPosition) {
                    pillboxViewModel.calendarMoveBackward()
                    if (loggingEnabled) {
                        Log.e("CalendarPagerAdapter", "createFragment: adjusting prev")
                    }
                } else {
                    pillboxViewModel.calendarMoveForward()
                    if (loggingEnabled) {
                        Log.e("CalendarPagerAdapter", "createFragment: adjusting next")
                    }
                }
            }
        }

        val data = when {
            position < lastPosition -> pillboxViewModel.getCalendarPrevDayData()
            position > lastPosition -> pillboxViewModel.getCalendarNextDayData()
            else -> pillboxViewModel.getCalendarCurrDayData()
        }

        when {
            position < lastPosition -> {
                pillboxViewModel.calendarMoveBackward()
                if (loggingEnabled) {
                    Log.i(
                        "CalendarPagerAdapter",
                        "position < lastPosition -> calendarMoveBackward()"
                    )

                    Log.i(
                        "CalendarPagerAdapter",
                        "createFragment: prev $lastPosition -> $position ${
                            DateTimeUtils.millisToDate(
                                data.first
                            )
                        }"
                    )
                }
            }

            position > lastPosition -> {
                pillboxViewModel.calendarMoveForward()
                if (loggingEnabled) {
                    Log.i(
                        "CalendarPagerAdapter",
                        "position > lastPosition -> calendarMoveForward()"
                    )


                Log.i(
                    "CalendarPagerAdapter",
                    "createFragment: next $lastPosition -> $position ${
                        DateTimeUtils.millisToDate(
                            data.first
                        )
                    }"
                )
                }
            }

            else -> {
                if (loggingEnabled) {
                    Log.i(
                        "CalendarPagerAdapter",
                        "createFragment: same $lastPosition -> $position ${
                            DateTimeUtils.millisToDate(
                                data.first
                            )
                        }"
                    )
                }
            }
        }

        lastPosition = position

        if (loggingEnabled) {
            Log.v("CalendarPagerAdapter", "return")
        }

        return CalendarPageFragment(data)
    }

}
