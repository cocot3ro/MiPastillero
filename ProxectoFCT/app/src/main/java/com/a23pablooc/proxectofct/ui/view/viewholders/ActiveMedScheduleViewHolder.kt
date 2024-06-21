package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatShortTime
import com.a23pablooc.proxectofct.databinding.SimpleScheduleItemBinding
import java.util.Date

class ActiveMedScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = SimpleScheduleItemBinding.bind(view)

    fun render(hora: Date) {
        binding.tv.text = hora.formatShortTime()
    }
}