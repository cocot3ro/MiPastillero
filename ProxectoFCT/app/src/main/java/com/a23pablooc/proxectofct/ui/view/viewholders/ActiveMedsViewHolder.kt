package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatDate
import com.a23pablooc.proxectofct.databinding.ActiveMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.a23pablooc.proxectofct.ui.view.adapters.ActiveMedScheduleRecyclerViewAdapter
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

class ActiveMedsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ActiveMedBinding.bind(view)
    private var adapter: ActiveMedScheduleRecyclerViewAdapter? = null

    fun render(
        med: MedicamentoActivoItem,
        onFav: (MedicamentoActivoItem) -> Unit,
        onInfo: (MedicamentoActivoItem) -> Unit
    ) {
        adapter = adapter ?: ActiveMedScheduleRecyclerViewAdapter(med.horario.toList())

        if (med.fkMedicamento.imagen.toString().isNotBlank()) {
            Glide.with(binding.root)
                .load(med.fkMedicamento.imagen)
                .into(binding.medImg)
        }

        binding.medName.text = med.fkMedicamento.nombre

        binding.dosis.text = med.dosis

        binding.ivFavBg.visibility = if (med.fkMedicamento.esFavorito) View.VISIBLE else View.GONE

        binding.favFrame.setOnClickListener {
            onFav(med)
            binding.ivFavBg.apply {
                visibility = visibility.xor(View.GONE)
            }
        }

        binding.btnInfo.setOnClickListener { onInfo(med) }

        binding.dateStart.text = med.fechaInicio.formatDate()
        binding.dateEnd.text = med.fechaFin.formatDate()

        binding.rvSchedule.apply {
            adapter = this@ActiveMedsViewHolder.adapter
            layoutManager = FlexboxLayoutManager(context).apply {
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.CENTER
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
        }

        adapter!!.updateData(med.horario.toList())
    }
}