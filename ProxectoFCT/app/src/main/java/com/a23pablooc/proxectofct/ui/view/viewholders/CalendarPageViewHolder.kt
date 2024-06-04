package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.databinding.CalendarMedGroupBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.CalendarMedRecyclerViewAdapter
import java.util.Date

class CalendarPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CalendarMedGroupBinding.bind(view)
    private var adapter: CalendarMedRecyclerViewAdapter? = null

    fun render(
        dia: Date,
        hora: Date,
        meds: List<MedicamentoActivoItem>,
        onClick: (MedicamentoActivoItem, Date, Date) -> Unit
    ) {
        adapter = adapter.takeIf { it != null } ?: CalendarMedRecyclerViewAdapter(dia, hora, meds, onClick)

        binding.hour.text = hora.formatTime()

        binding.calendarMedsLayout.apply {
            adapter = this@CalendarPageViewHolder.adapter
            layoutManager = LinearLayoutManager(context)
        }

        adapter!!.updateData(meds)
    }
}