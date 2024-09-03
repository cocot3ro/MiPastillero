package com.cocot3ro.mipastillero.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatShortTime
import com.cocot3ro.mipastillero.databinding.SimpleScheduleItemBinding
import java.util.Date

class ActiveMedScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = SimpleScheduleItemBinding.bind(view)

    fun render(hora: Date) {
        binding.tv.text = hora.formatShortTime()
    }
}