package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.databinding.MedHistoryBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.ScheduleRecyclerViewAdapter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = MedHistoryBinding.bind(view)
    private var adapter: ScheduleRecyclerViewAdapter? = null

    fun render(med: MedicamentoActivoItem) {
        adapter = adapter.takeIf { it != null } ?: ScheduleRecyclerViewAdapter(med.horario.toList())

        binding.dateStart.text = med.fechaInicio.formatDate()
        binding.dateEnd.text = med.fechaFin.formatDate()

        val tomas = med.tomas.map { it -> it.value.map { it.value } }.flatten().filter { it }.size
        val total = DateTimeUtils.daysBetweenDates(med.fechaInicio, med.fechaFin) * med.horario.size
        val txt = "$tomas / $total"
        binding.tvTomas.text = txt

        if (med.dosis.isBlank()) binding.clDosis.visibility = View.INVISIBLE
        else binding.tvDosis.text = med.dosis

        binding.rvSchedule.apply {
            adapter = this@HistoryViewHolder.adapter
            layoutManager = FlexboxLayoutManager(context).apply {
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.CENTER
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
        }
    }
}
