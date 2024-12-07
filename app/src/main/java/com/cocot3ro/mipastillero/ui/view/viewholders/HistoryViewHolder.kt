package com.cocot3ro.mipastillero.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.core.DateTimeUtils
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatDate
import com.cocot3ro.mipastillero.databinding.MedHistoryBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.ui.view.adapters.ScheduleRecyclerViewAdapter
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = MedHistoryBinding.bind(view)
    private var adapter: ScheduleRecyclerViewAdapter? = null

    fun render(med: MedicamentoActivoItem) {
        adapter = adapter ?: ScheduleRecyclerViewAdapter(med.horario.toList())

        binding.dateStart.text = med.fechaInicio.formatDate()
        binding.dateEnd.text = med.fechaFin.formatDate()

        val tomas = med.tomas.filter { it.value }.size
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
