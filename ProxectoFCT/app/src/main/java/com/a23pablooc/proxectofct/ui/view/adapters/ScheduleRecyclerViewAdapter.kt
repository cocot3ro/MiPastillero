package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.ui.view.diffutils.ScheduleItemDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.ScheduleItemViewHolder
import java.util.Date

class ScheduleRecyclerViewAdapter(
    private var list: List<Date>
) : RecyclerView.Adapter<ScheduleItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleItemViewHolder {
        return ScheduleItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.schedule_item, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ScheduleItemViewHolder, position: Int) {
        holder.render(list[position])
    }

    fun updateData(newData: List<Date>) {
        val diffUtil = ScheduleItemDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}