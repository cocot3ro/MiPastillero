package com.cocot3ro.mipastillero.ui.view.viewholders

import android.icu.util.Calendar
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cocot3ro.mipastillero.core.DateTimeUtils.get
import com.cocot3ro.mipastillero.databinding.CalendarMedBinding
import com.cocot3ro.mipastillero.domain.model.MedicamentoActivoItem
import java.util.Date

class CalendarMedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = CalendarMedBinding.bind(view)

    fun render(
        dia: Date,
        hora: Date,
        med: MedicamentoActivoItem,
        onMarcarToma: (MedicamentoActivoItem, Date, Date, () -> Unit) -> Unit
    ) {
        binding.medName.text = med.fkMedicamento.nombre

        binding.dosis.text = med.dosis

        binding.btnTake.apply {
            val timeStamp = Calendar.getInstance().apply {
                set(Calendar.YEAR, dia.get(Calendar.YEAR))
                set(Calendar.MONTH, dia.get(Calendar.MONTH))
                set(Calendar.DAY_OF_MONTH, dia.get(Calendar.DAY_OF_MONTH))
                set(Calendar.HOUR_OF_DAY, hora.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, hora.get(Calendar.MINUTE))
                set(Calendar.SECOND, hora.get(Calendar.SECOND))
                set(Calendar.MILLISECOND, hora.get(Calendar.MILLISECOND))
            }.time

            isChecked = med.tomas[timeStamp] ?: false

            setOnClickListener {
                val wasChecked = !isChecked
                onMarcarToma(med, dia, hora) {
                    isChecked = wasChecked
                }
            }
        }

        if (med.fkMedicamento.imagen.toString().isNotBlank()) {
            Glide.with(binding.root.context)
                .load(med.fkMedicamento.imagen)
                .into(binding.medImg)
        }
    }
}