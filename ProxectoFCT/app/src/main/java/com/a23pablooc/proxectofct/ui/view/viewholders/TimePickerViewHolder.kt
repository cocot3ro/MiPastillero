package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.databinding.TimePickerBinding
import java.util.Date

class TimePickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = TimePickerBinding.bind(view)

    private var firstShow = true

    fun render(
        date: Date,
        onSelectTime: (Date) -> Unit,
        onRemoveTimer: (Date) -> Unit,
        showOnAdd: Boolean
    ) {
        binding.hour.text = date.formatTime()

        binding.btnDeleteTimer.setOnClickListener {
            onRemoveTimer(date)
        }

        binding.btnSelectTime.setOnClickListener {
            onSelectTime(date)
        }

        if (firstShow && showOnAdd) {
            binding.btnSelectTime.performClick()
            firstShow = false
        }
    }
}