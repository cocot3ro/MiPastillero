package com.a23pablooc.proxectofct.ui.view.viewholders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.a23pablooc.proxectofct.core.DateTimeUtils.formatTime
import com.a23pablooc.proxectofct.databinding.CalendarMedBinding
import com.a23pablooc.proxectofct.databinding.CalendarMedGroupBinding
import com.a23pablooc.proxectofct.domain.model.MedicamentoActivoItem
import com.bumptech.glide.Glide
import java.util.Date

class CalendarPageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = CalendarMedGroupBinding.bind(view)

    fun render(
        dia: Date,
        hora: Date,
        meds: List<MedicamentoActivoItem>,
        onClick: (MedicamentoActivoItem, Date, Date) -> Unit
    ) {
        binding.hour.text = hora.formatTime()

        for (med in meds) {
            CalendarMedBinding.inflate(
                LayoutInflater.from(itemView.context), binding.calendarMedsLayout, true
            ).apply {
                if (med.fkMedicamento.imagen.toString().isNotBlank()) {
                    Glide.with(root.context)
                        .load(med.fkMedicamento.imagen)
                        .into(medImg)
                }

                medName.text = med.fkMedicamento.nombre

                btnTake.apply {
                    isChecked = med.tomas[dia]?.get(hora) ?: false
                    setOnClickListener {
                        onClick(med, dia, hora)
                    }
                }
            }
        }
    }
}