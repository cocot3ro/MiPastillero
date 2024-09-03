package com.cocot3ro.mipastillero.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cocot3ro.mipastillero.databinding.MedHistoryGroupBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import com.cocot3ro.mipastillero.domain.model.MedicamentoItem
import com.cocot3ro.mipastillero.ui.view.adapters.HistoryRecyclerViewAdapter
import com.bumptech.glide.Glide

class HistoryGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: MedHistoryGroupBinding = MedHistoryGroupBinding.bind(view)
    private var adapter: HistoryRecyclerViewAdapter? = null

    fun render(
        list: List<MedicamentoActivoItem>,
        onInfo: (MedicamentoItem) -> Unit
    ) {
        val med = list.first()

        adapter = adapter ?: HistoryRecyclerViewAdapter(list)

        binding.medName.text = med.fkMedicamento.nombre

        if (med.fkMedicamento.imagen.toString().isNotBlank()) {
            Glide.with(binding.medImg)
                .load(med.fkMedicamento.imagen)
                .into(binding.medImg)
        }

        binding.ibInfo.setOnClickListener { onInfo(med.fkMedicamento) }

        binding.rvMeds.apply {
            adapter = this@HistoryGroupViewHolder.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}