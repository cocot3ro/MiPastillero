package com.cocot3ro.mipastillero.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.ui.view.diffutils.CalendarPageDiffUtil
import com.cocot3ro.mipastillero.ui.view.viewholders.CalendarPageViewHolder
import java.util.Date

class CalendarPageMedGroupRecyclerViewAdapter(
    private val dia: Date,
    private var list: List<MedicamentoActivoItem>,
    private val onMarcarToma: (MedicamentoActivoItem, Date, Date, () -> Unit) -> Unit
) : RecyclerView.Adapter<CalendarPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarPageViewHolder {
        return CalendarPageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.calendar_med_group, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return getHorario(list).size
    }

    override fun onBindViewHolder(holder: CalendarPageViewHolder, position: Int) {
        val hora = getHorario(list)[position]

        holder.render(
            dia,
            hora,
            list.filter { it -> it.horario.any { it.time == hora.time } },
            onMarcarToma
        )
    }

    fun updateData(newData: List<MedicamentoActivoItem>) {
        val diffUtil = CalendarPageDiffUtil(list, newData)
        val result = DiffUtil.calculateDiff(diffUtil)
        list = newData
        result.dispatchUpdatesTo(this)
    }

    private fun getHorario(list: List<MedicamentoActivoItem>): List<Date> {
        return list.map { it.horario }
            .flatten()
            .distinctBy { it.time }
            .sortedBy { it.time }
    }
}