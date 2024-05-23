package com.a23pablooc.proxectofct.ui.view.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.a23pablooc.proxectofct.ui.view.fragments.CalendarPageFragment
import java.util.Calendar
import java.util.Date

class CalendarViewPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val offset = position - START_POSITION

        val date = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, offset)
        }.timeInMillis

        return CalendarPageFragment().apply {
            arguments = Bundle().apply {
                putLong(CalendarPageFragment.ARGS_DATE_KEY, date)
            }
        }
    }

    fun search(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DAY_OF_YEAR) - 1
    }

}