package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.MedHistoryGroupBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.HistoryRecyclerViewAdapter
import com.bumptech.glide.Glide

class HistoryGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: MedHistoryGroupBinding = MedHistoryGroupBinding.bind(view)
    private var adapter: HistoryRecyclerViewAdapter? = null

    fun render(
        list: List<MedicamentoActivoItem>,
        onInfo: (MedicamentoActivoItem) -> Unit
    ) {
        val med = list.first()

        adapter = adapter ?: HistoryRecyclerViewAdapter(list)

        binding.medName.text = med.fkMedicamento.nombre

        if (med.fkMedicamento.imagen.toString().isNotBlank()) {
            Glide.with(binding.medImg)
                .load(med.fkMedicamento.imagen)
                .into(binding.medImg)
        }

        binding.ibInfo.setOnClickListener { onInfo(med) }

        binding.rvMeds.apply {
            adapter = this@HistoryGroupViewHolder.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}