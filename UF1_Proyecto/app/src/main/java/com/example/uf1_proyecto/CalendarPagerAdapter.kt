package com.example.uf1_proyecto

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.math.abs

// TODO: Borrar logs

class CalendarPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val loggingEnabled: Boolean = false
) : FragmentStateAdapter(fragmentActivity) {

    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    private var _lastPosition: Int? = null
    val lastPosition get() = _lastPosition!!
    private lateinit var origin: CalendarPagerAdapter

    companion object {
        @Volatile
        private var instance: CalendarPagerAdapter? = null
        fun getInstance(fragmentActivity: FragmentActivity): CalendarPagerAdapter {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = CalendarPagerAdapter(fragmentActivity)
                    }
                }
            }

            return instance!!.clone(fragmentActivity)
        }
    }

    private fun clone(fragmentActivity: FragmentActivity): CalendarPagerAdapter {
        val calendarPagerAdapter = CalendarPagerAdapter(fragmentActivity, loggingEnabled)
        calendarPagerAdapter._lastPosition = this.lastPosition
        calendarPagerAdapter.origin = this

        if(loggingEnabled) {
            Log.v("CalendarPagerAdapter", "origin: $this")
            Log.v("CalendarPagerAdapter", "clone: $calendarPagerAdapter")
        }

        return calendarPagerAdapter
    }

    init {
        if (loggingEnabled) {
            Log.v("CalendarPagerAdapter", "init")
        }
        _pillboxViewModel = PillboxViewModel.getInstance(fragmentActivity)
        _lastPosition = itemCount / 2
    }

    override fun toString(): String {
        return super.toString() + " lastPosition: $lastPosition"
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    private fun movePosition(position: Int) {
        _lastPosition = position
        origin._lastPosition = position
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
                    "createFragment: abs(position - lastPosition) = ${abs(position - lastPosition)} (${position - lastPosition})"
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
                .also { pillboxViewModel.calendarMoveBackward() }

            position > lastPosition -> pillboxViewModel.getCalendarNextDayData()
                .also { pillboxViewModel.calendarMoveForward() }

            else -> pillboxViewModel.getCalendarCurrDayData()
        }

        if (loggingEnabled) {
            when {
                position < lastPosition -> {
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

                position > lastPosition -> {
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

                else -> {
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

        movePosition(position)

        if (loggingEnabled) {
            Log.v("CalendarPagerAdapter", "return")
        }

        return CalendarPageFragment(data, position)
    }

}
