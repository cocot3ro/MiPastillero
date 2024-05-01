package com.a23pablooc.proxectofct.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.a23pablooc.proxectofct.utils.DateTimeUtils

class CalendarPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity), PagerAdapter {

    private var fragmentList: MutableList<CalendarPageFragment> = mutableListOf()

    companion object {
        @Volatile
        private var instance: CalendarPagerAdapter? = null

        const val START_POSITION = Int.MAX_VALUE / 2

        fun getInstance(fragmentActivity: FragmentActivity): CalendarPagerAdapter {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = CalendarPagerAdapter(fragmentActivity)
                    }
                }
            }

            return clone(fragmentActivity, instance!!)
        }

        private fun clone(
            fragmentActivity: FragmentActivity,
            origin: CalendarPagerAdapter
        ): CalendarPagerAdapter {
            val clone = CalendarPagerAdapter(fragmentActivity)
            clone.fragmentList = origin.fragmentList
            return clone
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        val offset = position - START_POSITION
        val today = DateTimeUtils.getTodayAsMillis()

        val fragmentDate = DateTimeUtils.moveDay(today, offset)

        return CalendarPageFragment(fragmentDate).apply {
            fragmentList.add(this)
            this.setOnDestroyListener {
                fragmentList.remove(this)
            }
        }
    }

    override fun search(date: Long): Int {
        val offset = DateTimeUtils.daysBetweenMillis(DateTimeUtils.getTodayAsMillis(), date)
        return START_POSITION + offset
    }

    override fun reload() {
        fragmentList.forEach(CalendarPageFragment::updateView)
    }

}
