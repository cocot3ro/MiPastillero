package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.databinding.CalendarMedBinding
import com.a23pablooc.proxectofct.databinding.CalendarMedGroupBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoCalendarioItem
import com.bumptech.glide.Glide

class CalendarPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CalendarMedGroupBinding.bind(view)

    fun render(
        meds: List<MedicamentoCalendarioItem>,
        onClick: (MedicamentoCalendarioItem) -> Unit
    ) {
        // TODO: colores?

        // binding.body.setBackgroundColor()

        binding.hour.text = meds[0].hora.toString()

        for (med in meds) {
            CalendarMedBinding.inflate(LayoutInflater.from(itemView.context), binding.body, true)
                .apply {
                    if (med.medicamento.imagen.isNotEmpty()) {
                        Glide.with(root.context)
                            .load(med.medicamento.imagen)
                            .into(medImg)
                    }

                    medName.text = med.medicamento.nombre

                    btnTake.apply {
                        isChecked = med.seHaTomado
                        setOnClickListener {
                            onClick(med)
                        }
                    }
                }
        }
    }
}