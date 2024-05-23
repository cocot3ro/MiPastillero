package com.a23pablooc.proxectofct.old.old_view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.a23pablooc.proxectofct.old.old_utils.DateTimeUtils

@Deprecated("old")
class DiaryPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity), PagerAdapter {

    private var fragmentList: MutableList<DiaryPageFragment> = mutableListOf()

    companion object {
        @Volatile
        private var instance: DiaryPagerAdapter? = null

        const val START_POSITION = Int.MAX_VALUE / 2

        fun getInstance(fragmentActivity: FragmentActivity): DiaryPagerAdapter {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = DiaryPagerAdapter(fragmentActivity)
                    }
                }
            }

            return clone(fragmentActivity, instance!!)
        }

        private fun clone(
            fragmentActivity: FragmentActivity,
            origin: DiaryPagerAdapter
        ): DiaryPagerAdapter {
            val clone = DiaryPagerAdapter(fragmentActivity)
            clone.fragmentList = origin.fragmentList
            return clone
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        val today = DateTimeUtils.getTodayAsMillis()
        val offset = position - START_POSITION

        val fragmentDate = DateTimeUtils.moveDay(today, offset)

        return DiaryPageFragment(fragmentDate).apply {
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
        fragmentList.forEach(DiaryPageFragment::changeToRenderer)
    }

}
