package com.a23pablooc.proxectofct.ui.view.adapters

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.zeroTime
import com.a23pablooc.proxectofct.ui.view.fragments.DiaryPageFragment

class DiaryViewPagerAdapter (
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        const val START_POSITION: Int = Int.MAX_VALUE / 2
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        val offset = position - CalendarViewPagerAdapter.START_POSITION

        val date = Calendar.getInstance().apply {
            time = DateTimeUtils.today.zeroTime()
            add(Calendar.DAY_OF_YEAR, offset)
        }.time

        return DiaryPageFragment.newInstance(date)
    }
}