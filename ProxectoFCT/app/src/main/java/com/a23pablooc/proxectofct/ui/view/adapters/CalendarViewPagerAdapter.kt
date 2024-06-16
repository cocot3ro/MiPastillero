package com.a23pablooc.proxectofct.ui.view.adapters

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.ui.view.fragments.CalendarPageFragment

class CalendarViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    companion object {
        const val START_POSITION: Int = Int.MAX_VALUE / 2
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val offset = position - START_POSITION

        val date = Calendar.getInstance().apply {
            time = DateTimeUtils.now

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            add(Calendar.DAY_OF_YEAR, offset)
        }.time

        return CalendarPageFragment.newInstance(date)
    }
}