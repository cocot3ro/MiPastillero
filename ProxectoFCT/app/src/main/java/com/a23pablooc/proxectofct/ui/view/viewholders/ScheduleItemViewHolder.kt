package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.databinding.ScheduleItemBinding
import java.util.Date

class ScheduleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ScheduleItemBinding.bind(view)

    fun render(date: Date) {
        binding.hour.text = date.formatTime()
    }
}