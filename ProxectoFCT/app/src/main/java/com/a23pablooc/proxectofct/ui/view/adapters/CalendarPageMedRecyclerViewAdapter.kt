package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.diffutils.CalendarMedDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.CalendarMedViewHolder
import java.util.Date

class CalendarPageMedRecyclerViewAdapter(
    private val dia: Date,
    private var hora: Date,
    private var list: List<MedicamentoActivoItem>,
    private val onTake: (MedicamentoActivoItem, Date, Date) -> Unit
) : RecyclerView.Adapter<CalendarMedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarMedViewHolder {
        return CalendarMedViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.calendar_med, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CalendarMedViewHolder, position: Int) {
        holder.render(dia, hora, list[position], onTake)
    }

    fun updateData(newData: List<MedicamentoActivoItem>) {
        val diffUtil = CalendarMedDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        this.list = newData
        result.dispatchUpdatesTo(this)
    }
}