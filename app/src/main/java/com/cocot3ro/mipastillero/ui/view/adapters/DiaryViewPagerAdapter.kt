package com.cocot3ro.mipastillero.ui.view.adapters

import android.icu.util.Calendar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.ui.view.fragments.DiaryPageFragment

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
            time = DateTimeUtils.now
            add(Calendar.DAY_OF_YEAR, offset)
        }.time

        return DiaryPageFragment.newInstance(date)
    }
}