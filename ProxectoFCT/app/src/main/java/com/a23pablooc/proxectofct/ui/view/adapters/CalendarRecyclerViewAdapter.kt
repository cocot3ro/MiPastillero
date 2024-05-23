package com.a23pablooc.proxectofct.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import com.a23pablooc.proxectofct.ui.view.diffutils.CalendarDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.CalendarViewHolder

class CalendarRecyclerViewAdapter(
    private var list: List<MedicamentoCalendarioItem>,
    private val onClick: (MedicamentoCalendarioItem) -> Unit
) : RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        return CalendarViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_med_group, parent, false)
        )
    }

    override fun getItemCount(): Int = list.distinctBy { it.hora }.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val hora = list.distinctBy { it.fecha }[position].hora

        holder.render(list.filter { it.hora == hora }, onClick)
    }

    fun updateData(newData: List<MedicamentoCalendarioItem>) {
        val diffUtil = CalendarDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }
}