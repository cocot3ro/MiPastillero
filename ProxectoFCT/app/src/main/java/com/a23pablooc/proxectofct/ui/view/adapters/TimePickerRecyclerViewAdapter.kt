package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.ui.view.diffutils.TimePickerDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.TimePickerViewHolder
import java.util.Date

class TimePickerRecyclerViewAdapter(
    private var list: List<Date>,
    private val onSelectTime: (Date) -> Unit,
    private val onRemoveTimer: (Date) -> Unit
) : RecyclerView.Adapter<TimePickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimePickerViewHolder {
        return TimePickerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.time_picker, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: TimePickerViewHolder, position: Int) {
        holder.render(list[position], onSelectTime, onRemoveTimer)
    }

    fun updateData(newData: List<Date>) {
        val diffUtil = TimePickerDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }

}