package com.a23pablooc.proxectofct.ui.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.R
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.diffutils.CalendarPageDiffUtil
import com.a23pablooc.proxectofct.ui.view.viewholders.CalendarPageViewHolder
import java.util.Date

class CalendarRecyclerViewAdapter(
    private val dia: Date,
    private var list: List<MedicamentoActivoItem>,
    private val onClick: (MedicamentoActivoItem, Date, Date) -> Unit
) : RecyclerView.Adapter<CalendarPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.calendar_med_group, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return getHorario(list, "getItemCount(list)").size
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        Log.v("CalendarRecyclerViewAdapter", "itemCount: $itemCount")

        val hora = getHorario(list, "onBindViewHolder(list)")[position]

        Log.v(
            "CalendarRecyclerViewAdapter",
            "binding dia: ${dia.formatDate()}, hora: ${hora.formatTime()}"
        )

        holder.render(
            dia,
            hora,
            list.filter { it -> it.horario.any { it.time == hora.time } },
            onClick
        )
    }

    fun updateData(newData: List<MedicamentoActivoItem>) {
        Log.v("CalendarRecyclerViewAdapter", "updateData")
        Log.v("CalendarRecyclerViewAdapter", "old list: ${list.map { it.hashCode() }}")
        Log.v("CalendarRecyclerViewAdapter", "new list: ${newData.map { it.hashCode() }}")

        val diffUtil = CalendarPageDiffUtil(list, newData)
        Log.v("CalendarRecyclerViewAdapter", "diffUtil created")
        val result = DiffUtil.calculateDiff(diffUtil)
        Log.v("CalendarRecyclerViewAdapter", "diffUtil calculated")
        list = newData
        Log.v("CalendarRecyclerViewAdapter", "before dispatching")
        result.dispatchUpdatesTo(this)
        Log.v("CalendarRecyclerViewAdapter", "after dispatching")
    }

    private fun getHorario(list: List<MedicamentoActivoItem>, tag: String): List<Date> {
        return list.map { it.horario }
            .flatten()
            .distinctBy { it.time }
            .sortedBy { it.time }.also {
                Log.e(
                    "CalendarRecyclerViewAdapter",
                    "$tag -> getHorario: ${it.map { d -> d.formatTime() }}"
                )
            }
    }
}