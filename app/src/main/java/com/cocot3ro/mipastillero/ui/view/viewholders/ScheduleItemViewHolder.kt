package com.cocot3ro.mipastillero.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatShortTime
import com.cocot3ro.mipastillero.databinding.ScheduleItemBinding
import java.util.Date

class ScheduleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ScheduleItemBinding.bind(view)

    fun render(date: Date) {
        binding.hour.text = date.formatShortTime()
    }
}