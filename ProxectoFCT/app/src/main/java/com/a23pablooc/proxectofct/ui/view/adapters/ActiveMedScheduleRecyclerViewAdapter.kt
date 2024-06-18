package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.ui.view.diffutils.ActiveMedScheduleDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.ActiveMedScheduleViewHolder
import java.util.Date

class ActiveMedScheduleRecyclerViewAdapter(
    private var list: List<Date>
) : RecyclerView.Adapter<ActiveMedScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveMedScheduleViewHolder {
        return ActiveMedScheduleViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.simple_schedule_item, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ActiveMedScheduleViewHolder, position: Int) {
        holder.render(list[position])
    }

    fun updateData(newData: List<Date>) {
        val diffUtil = ActiveMedScheduleDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        this.list = newData
        result.dispatchUpdatesTo(this)
    }
}