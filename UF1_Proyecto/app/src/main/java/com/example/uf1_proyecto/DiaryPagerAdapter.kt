package com.example.uf1_proyecto

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.math.abs

class DiaryPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val loggingEnabled: Boolean = false
) :
    FragmentStateAdapter(fragmentActivity) {
    private var _pillboxViewModel: PillboxViewModel? = null
    private val pillboxViewModel get() = _pillboxViewModel!!

    var lastPosition: Int

    init {
        if (loggingEnabled) {
            Log.v("DiaryPagerAdapter", "init")
        }
        _pillboxViewModel = PillboxViewModel.getInstance(fragmentActivity)

        lastPosition = itemCount / 2
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        if (loggingEnabled) {
            Log.i("DiaryPagerAdapter", "")
            Log.i("DiaryPagerAdapter", "createFragment: $lastPosition -> $position")
        }

        if (abs(position - lastPosition) > 1) {
            if (loggingEnabled) {
                Log.d(
                    "DiaryPagerAdapter",
                    "createFragment: abs(position - lastPosition) ${abs(position - lastPosition)}"
                )
            }
            for (i in 1 until abs(position - lastPosition)) {
                if (position < lastPosition) {
                    pillboxViewModel.diaryMoveBackward()
                    if (loggingEnabled) {
                        Log.e("DiaryPagerAdapter", "createFragment: adjusting prev")
                    }
                } else {
                    pillboxViewModel.diaryMoveForward()
                    if (loggingEnabled) {
                        Log.e("DiaryPagerAdapter", "createFragment: adjusting next")
                    }
                }
            }
        }

        val data = when {
            position < lastPosition -> pillboxViewModel.getDiaryPrevDayData()
            position > lastPosition -> pillboxViewModel.getDiaryNextDayData()
            else -> pillboxViewModel.getDiaryCurrDayData()
        }

        when {
            position < lastPosition -> {
                pillboxViewModel.diaryMoveBackward()
                if (loggingEnabled) {
                    Log.i(
                        "DiaryPagerAdapter",
                        "position < lastPosition -> diaryMoveBackward()"
                    )

                    Log.i(
                        "DiaryPagerAdapter",
                        "createFragment: prev $lastPosition -> $position ${
                            DateTimeUtils.millisToDate(
                                data.first
                            )
                        }"
                    )
                }
            }

            position > lastPosition -> {
                pillboxViewModel.diaryMoveForward()
                if (loggingEnabled) {
                    Log.i(
                        "DiaryPagerAdapter",
                        "position > lastPosition -> diaryMoveForward()"
                    )


                    Log.i(
                        "DiaryPagerAdapter",
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
                        "DiaryPagerAdapter",
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
            Log.v("DiaryPagerAdapter", "return")
        }

        return DiaryPageFragment(data)
    }

}
