package com.cocot3ro.mipastillero.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.core.DateTimeUtils.formatShortTime
import com.cocot3ro.mipastillero.databinding.CalendarMedGroupBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.ui.view.adapters.CalendarPageMedRecyclerViewAdapter
import java.util.Date

class CalendarPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CalendarMedGroupBinding.bind(view)
    private var adapter: CalendarPageMedRecyclerViewAdapter? = null

    fun render(
        dia: Date,
        hora: Date,
        meds: List<MedicamentoActivoItem>,
        onMarcarToma: (MedicamentoActivoItem, Date, Date) -> Unit
    ) {
        adapter = adapter ?: CalendarPageMedRecyclerViewAdapter(dia, hora, meds, onMarcarToma)

        binding.hour.text = hora.formatShortTime()

        binding.rvMeds.apply {
            adapter = this@CalendarPageViewHolder.adapter
            layoutManager = LinearLayoutManager(context)
        }

        adapter!!.updateData(meds)
    }
}