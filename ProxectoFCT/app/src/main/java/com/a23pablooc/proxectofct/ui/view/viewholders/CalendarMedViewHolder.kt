package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.CalendarMedBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.bumptech.glide.Glide
import java.util.Date

class CalendarMedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = CalendarMedBinding.bind(view)

    fun render(
        dia: Date,
        hora: Date,
        med: MedicamentoActivoItem,
        onTake: (MedicamentoActivoItem, Date, Date) -> Unit
    ) {
        binding.medName.text = med.fkMedicamento.nombre

        binding.dosis.text = med.dosis

        binding.btnTake.apply {
            isChecked = med.tomas[dia]?.get(hora) ?: false
            setOnClickListener {
                onTake(med, dia, hora)
            }
        }

        if (med.fkMedicamento.imagen.toString().isNotBlank()) {
            Glide.with(binding.root.context)
                .load(med.fkMedicamento.imagen)
                .into(binding.medImg)
        }
    }
}