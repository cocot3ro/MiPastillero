package com.cocot3ro.mipastillero.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatShortTime
import com.cocot3ro.mipastillero.databinding.TimePickerBinding
import java.util.Date

class TimePickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = TimePickerBinding.bind(view)

    fun render(
        date: Date,
        onSelectTime: (Date) -> Unit,
        onRemoveTimer: (Date) -> Unit
    ) {
        binding.hour.text = date.formatShortTime()

        binding.btnDeleteTimer.setOnClickListener { onRemoveTimer(date) }

        binding.btnSelectTime.setOnClickListener { onSelectTime(date) }
    }
}