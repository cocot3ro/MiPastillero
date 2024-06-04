package com.a23pablooc.proxectofct.ui.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.ui.view.fragments.CalendarPageFragment
import android.icu.util.Calendar

class CalendarViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val offset = position - START_POSITION

        val date = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, offset)
        }.time.zeroTime()

        return CalendarPageFragment.newInstance(date)
    }

}